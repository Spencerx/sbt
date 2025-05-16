/**
 * This code is generated using [[https://www.scala-sbt.org/contraband/ sbt-contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.sona.codec
trait JsonProtocol extends sjsonnew.BasicJsonProtocol
  with sbt.internal.sona.codec.DeploymentStateFormats
  with sbt.internal.sona.codec.PublisherStatusFormats
object JsonProtocol extends JsonProtocol