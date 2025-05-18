/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 */

package sbt
package internal
package sona

import gigahorse.*, support.apachehttp.Gigahorse
import java.util.Base64
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import sbt.util.Logger
import sjsonnew.JsonFormat
import sjsonnew.support.scalajson.unsafe.{ Converter, Parser }
import sjsonnew.shaded.scalajson.ast.unsafe.JValue

import scala.annotation.nowarn
import scala.concurrent.*, duration.*

class Sona(client: SonaClient) extends AutoCloseable {
  def uploadBundle(
      bundleZipPath: Path,
      deploymentName: String,
      pt: PublishingType,
      log: Logger,
  ): Unit = {
    val deploymentId = client.uploadBundle(bundleZipPath, deploymentName, pt, log)
    client.waitForDeploy(deploymentId, pt, log)
  }
  def close(): Unit = client.close()
}

class SonaClient(reqTransform: Request => Request) extends AutoCloseable {
  import SonaClient.baseUrl

  val gigahorseConfig = Gigahorse.config
    .withRequestTimeout(2.minute)
    .withReadTimeout(2.minute)
  val http = Gigahorse.http(gigahorseConfig)
  def uploadBundle(
      bundleZipPath: Path,
      deploymentName: String,
      publishingType: PublishingType,
      log: Logger,
  ): String = {
    val res = retryF(maxAttempt = 2) { (attempt: Int) =>
      log.info(s"uploading bundle to Sonatype Central (attempt: $attempt)")
      val req = Gigahorse
        .url(s"${baseUrl}/publisher/upload")
        .addQueryString(
          "name" -> deploymentName,
          "publishingType" -> (publishingType match {
            case PublishingType.Automatic   => "AUTOMATIC"
            case PublishingType.UserManaged => "USER_MANAGED"
          })
        )
        .post(
          MultipartFormBody(
            FormPart("bundle", bundleZipPath.toFile())
          )
        )
        .withRequestTimeout(600.second)
      http.run(reqTransform(req), Gigahorse.asString)
    }
    awaitWithMessage(res, "uploading...", log)
  }

  def waitForDeploy(
      deploymentId: String,
      publishingType: PublishingType,
      log: Logger,
  ): Unit = {
    val status = deploymentStatus(deploymentId)
    log.info(s"deployment $deploymentId ${status.deploymentState}")
    status.deploymentState match {
      case DeploymentState.FAILED => sys.error(s"deployment $deploymentId failed")
      case DeploymentState.PENDING | DeploymentState.PUBLISHING | DeploymentState.VALIDATING =>
        Thread.sleep(5000)
        waitForDeploy(deploymentId, publishingType, log)
      case DeploymentState.PUBLISHED if publishingType == PublishingType.Automatic   => ()
      case DeploymentState.VALIDATED if publishingType == PublishingType.UserManaged => ()
      case _ =>
        Thread.sleep(5000)
        waitForDeploy(deploymentId, publishingType, log)
    }
  }

  def deploymentStatus(deploymentId: String): PublisherStatus = {
    val res = retryF(maxAttempt = 5) { (attempt: Int) =>
      deploymentStatusF(deploymentId)
    }
    Await.result(res, 600.seconds)
  }

  /** https://central.sonatype.org/publish/publish-portal-api/#verify-status-of-the-deployment
   */
  def deploymentStatusF(deploymentId: String): Future[PublisherStatus] = {
    val req = Gigahorse
      .url(s"${baseUrl}/publisher/status")
      .addQueryString("id" -> deploymentId)
      .post("", StandardCharsets.UTF_8)
    http.run(reqTransform(req), SonaClient.asPublisherStatus)
  }

  /** Retry future function on any error.
   */
  @nowarn
  def retryF[A1](maxAttempt: Int)(f: Int => Future[A1]): Future[A1] = {
    import scala.concurrent.ExecutionContext.Implicits.*
    def impl(retry: Int): Future[A1] = {
      val res = f(retry + 1)
      res.recoverWith {
        case _ if retry < maxAttempt =>
          Thread.sleep(5000)
          impl(retry + 1)
      }
    }
    impl(0)
  }

  def awaitWithMessage[A1](f: Future[A1], msg: String, log: Logger): A1 = {
    import scala.concurrent.ExecutionContext.Implicits.*
    def loop(attempt: Int): Unit =
      if (!f.isCompleted) {
        if (attempt > 0) {
          log.info(msg)
        }
        Future {
          blocking {
            Thread.sleep(30.second.toMillis)
          }
        }.foreach(_ => loop(attempt + 1))
      } else ()
    loop(0)
    Await.result(f, 600.seconds)
  }

  def close(): Unit = http.close()
}

object Sona {
  def host: String = SonaClient.host
  def oauthClient(userName: String, userToken: String): Sona =
    new Sona(SonaClient.oauthClient(userName, userToken))
}

object SonaClient {
  import sbt.internal.sona.codec.JsonProtocol.{ *, given }
  val host: String = "central.sonatype.com"
  val baseUrl: String = s"https://$host/api/v1"
  val asJson: FullResponse => JValue = (r: FullResponse) =>
    Parser.parseFromByteBuffer(r.bodyAsByteBuffer).get
  def as[A1: JsonFormat]: FullResponse => A1 = asJson.andThen(Converter.fromJsonUnsafe[A1])
  val asPublisherStatus: FullResponse => PublisherStatus = as[PublisherStatus]
  def oauthClient(userName: String, userToken: String): SonaClient =
    new SonaClient(OAuthClient(userName, userToken))
}

private case class OAuthClient(userName: String, userToken: String)
    extends Function1[Request, Request] {
  val base64Credentials =
    Base64.getEncoder.encodeToString(s"${userName}:${userToken}".getBytes(StandardCharsets.UTF_8))
  def apply(request: Request): Request =
    request.addHeaders("Authorization" -> s"Bearer $base64Credentials")
  override def toString: String = "OAuthClient(****)"
}

sealed trait PublishingType
object PublishingType {
  case object Automatic extends PublishingType
  case object UserManaged extends PublishingType
}
