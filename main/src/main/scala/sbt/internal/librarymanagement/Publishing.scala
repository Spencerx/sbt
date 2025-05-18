/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 */

package sbt
package internal
package librarymanagement

import java.nio.file.Path
import sbt.internal.util.MessageOnlyException
import sbt.io.IO
import sbt.io.Path.contentOf
import sbt.librarymanagement.ivy.Credentials
import sona.{ Sona, PublishingType }

object Publishing {
  val sonaRelease: Command =
    Command.command("sonaRelease")(sonatypeReleaseAction(PublishingType.Automatic))

  val sonaUpload: Command =
    Command.command("sonaUpload")(sonatypeReleaseAction(PublishingType.UserManaged))

  def makeBundle(stagingDir: Path, bundlePath: Path): Path = {
    if (bundlePath.toFile().exists()) {
      IO.delete(bundlePath.toFile())
    }
    IO.zip(
      sources = contentOf(stagingDir.toFile()),
      outputZip = bundlePath.toFile(),
      time = Some(0L),
    )
    bundlePath
  }

  private def sonatypeReleaseAction(pt: PublishingType)(s0: State): State = {
    val extracted = Project.extract(s0)
    val log = extracted.get(Keys.sLog)
    val dn = extracted.get(Keys.sonaDeploymentName)
    val (s1, bundle) = extracted.runTask(Keys.sonaBundle, s0)
    val (s2, creds) = extracted.runTask(Keys.credentials, s1)
    val client = fromCreds(creds)
    try {
      client.uploadBundle(bundle.toPath(), dn, pt, log)
      s2
    } finally {
      client.close()
    }
  }

  private def fromCreds(creds: Seq[Credentials]): Sona = {
    val cred = Credentials
      .forHost(creds, Sona.host)
      .getOrElse(throw new MessageOnlyException(s"no credentials are found for ${Sona.host}"))
    Sona.oauthClient(cred.userName, cred.passwd)
  }
}
