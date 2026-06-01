/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.internal.protocol
/** A general message as defined by JSON-RPC. */
abstract class JsonRpcMessage(
  val jsonrpc: String) extends Serializable {
  
  
  
  
  override def toString: String = {
    "JsonRpcMessage(" + jsonrpc + ")"
  }
}
object JsonRpcMessage {
  
}
