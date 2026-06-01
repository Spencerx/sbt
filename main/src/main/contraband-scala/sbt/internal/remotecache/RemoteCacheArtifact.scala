/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.remotecache
abstract class RemoteCacheArtifact(
  val artifact: sbt.librarymanagement.Artifact,
  val packaged: sbt.TaskKey[xsbti.HashedVirtualFileRef]) extends Serializable {
  
  
  
  
  override def toString: String = {
    "RemoteCacheArtifact(" + artifact + ", " + packaged + ")"
  }
}
object RemoteCacheArtifact {
  
}
