/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 */

package sbt
package internal

import org.scalasbt.shadedgson.com.google.gson.Gson
import java.io.*
import sbt.io.IO
import sbt.internal.worker1.*
import sbt.testing.Framework
import scala.sys.process.{ BasicIO, Process, ProcessIO }
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ Await, Promise }
import scala.concurrent.duration.*

object WorkerExchange:
  val listeners: mutable.ListBuffer[WorkerResponseListener] = ListBuffer.empty

  /**
   * Start a worker process.
   */
  def startWorker(fo: ForkOptions, extraCp: Seq[File]): WorkerProxy =
    val fullCp = Seq(
      IO.classLocationPath(classOf[WorkerMain]).toFile,
      IO.classLocationPath(classOf[Framework]).toFile,
      IO.classLocationPath(classOf[Gson]).toFile,
    ) ++ extraCp
    val options = Seq(
      "-classpath",
      fullCp.mkString(File.pathSeparator),
      classOf[WorkerMain].getCanonicalName,
    )
    val inputRef = Promise[OutputStream]()
    val processIo = ProcessIO(
      in = (input) => inputRef.success(input),
      out = BasicIO.processFully(onStdoutLine),
      err = BasicIO.processFully((line) => scala.Console.err.println(line)),
    )
    val forkWithIo = fo.withOutputStrategy(OutputStrategy.CustomInputOutput(processIo))
    val p = Fork.java.fork(forkWithIo, options)
    val forkTimeout = 30.seconds
    val input = Await.result(inputRef.future, forkTimeout)
    WorkerProxy(input, p, options)

  def registerListener(listener: WorkerResponseListener): Unit =
    synchronized:
      listeners.append(listener)

  def unregisterListener(listener: WorkerResponseListener): Unit =
    synchronized:
      if listeners.contains(listener) then listeners.remove(listeners.indexOf(listener))
      else ()

  /**
   * Unified worker output handler.
   */
  def onStdoutLine(line: String): Unit =
    synchronized:
      listeners.foreach: wl =>
        wl(line)
end WorkerExchange

class WorkerProxy(input: OutputStream, val process: Process, val options: Seq[String])
    extends AutoCloseable:
  lazy val inputStream = PrintStream(input)
  def close(): Unit = input.close()
  def blockForExitCode(): Int =
    if !process.isAlive then process.exitValue()
    else Fork.blockForExitCode(process)

  /** print a line into stdin of the worker process. */
  def println(str: String): Unit =
    inputStream.println(str)
    inputStream.flush()

  val watch = Thread(() => {
    while process.isAlive() do Thread.sleep(100)
    WorkerExchange.listeners.foreach(_.notifyExit(process))
  })
  watch.start()
end WorkerProxy

abstract class WorkerResponseListener extends Function1[String, Unit]:
  def notifyExit(p: Process): Unit
