/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt
enum ConnectionType {
  /** This uses Unix domain socket on POSIX, and named pipe on Windows. */
  case Local
  case Tcp
}
object ConnectionType {
  
}
