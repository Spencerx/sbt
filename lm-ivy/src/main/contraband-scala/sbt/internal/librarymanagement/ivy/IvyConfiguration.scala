/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.librarymanagement.ivy
abstract class IvyConfiguration(
  val lock: Option[xsbti.GlobalLock],
  val log: Option[xsbti.Logger],
  val updateOptions: sbt.internal.librarymanagement.ivy.UpdateOptions) extends Serializable {
  
  def this() = this(None, None, sbt.internal.librarymanagement.ivy.UpdateOptions())
  
  
  override def toString: String = {
    "IvyConfiguration(" + lock + ", " + log + ", " + updateOptions + ")"
  }
}
object IvyConfiguration {
  
}
