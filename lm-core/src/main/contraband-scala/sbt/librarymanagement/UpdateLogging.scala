/**
 * This code is generated using [[https://www.scala-sbt.org/contraband]].
 */

// DO NOT EDIT MANUALLY
package sbt.librarymanagement
/**
 * Configures logging during an 'update'.  `level` determines the amount of other information logged.
 * `Full` is the default and logs the most.
 * `DownloadOnly` only logs what is downloaded.
 * `Quiet` only displays errors.
 * `Default` uses the current log level of `update` task.
 */
enum UpdateLogging {
  
  case Full
  case DownloadOnly
  case Quiet
  case Default
}
object UpdateLogging {
  
}
