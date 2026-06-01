/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.util
abstract class AbstractEntry(
  val channelName: Option[String],
  val execId: Option[String]) extends Serializable {
  
  
  
  
  override def toString: String = {
    "AbstractEntry(" + channelName + ", " + execId + ")"
  }
}
object AbstractEntry {
  
}
