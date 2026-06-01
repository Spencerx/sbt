/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.langserver
/** Goto definition params model */
abstract class TextDocumentPositionParamsInterface(
  val textDocument: sbt.internal.langserver.TextDocumentIdentifier,
  val position: sbt.internal.langserver.Position) extends Serializable {
  
  
  
  
  override def toString: String = {
    "TextDocumentPositionParamsInterface(" + textDocument + ", " + position + ")"
  }
}
object TextDocumentPositionParamsInterface {
  
}
