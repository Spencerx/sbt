/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 */

package sbt
package internal
package server

import java.io.File
import sbt.BuildSyntax._
import sbt.Def._
import sbt.Keys._
import sbt.SlashSyntax0._
import sbt.internal.util.complete.Parser
import sbt.internal.worker.{ ClientJobParams, FilePath, JvmRunInfo, RunInfo }
import sbt.io.IO
import sbt.protocol.Serialization

/**
 * A ClientJob represents a unit of work that sbt server process
 * can outsourse back to the client. Initially intended for sbtn client-side run.
 */
object ClientJob {
  lazy val globalSettings: Seq[Def.Setting[_]] = Seq(
    clientJob := clientJobTask.evaluated,
    clientJob / aggregate := false,
  )

  private def clientJobTask: Def.Initialize[InputTask[ClientJobParams]] = Def.inputTaskDyn {
    val tokens = spaceDelimited().parsed
    val state = Keys.state.value
    val p = Act.aggregatedKeyParser(state)
    if (tokens.isEmpty) {
      sys.error("expected an argument, for example foo/run")
    }
    val scopedKey = Parser.parse(tokens.head, p) match {
      case Right(x :: Nil) => x
      case Right(xs)       => sys.error("too many keys")
      case Left(err)       => sys.error(err)
    }
    if (scopedKey.key == run.key)
      clientJobRunInfo.in(scopedKey.scope).toTask(" " + tokens.tail.mkString(" "))
    else sys.error(s"unsupported task for clientJob $scopedKey")
  }

  // This will be scoped to Compile, Test, etc
  lazy val configSettings: Seq[Def.Setting[_]] = Seq(
    clientJobRunInfo := clientJobRunInfoTask.evaluated,
  )

  private def clientJobRunInfoTask: Def.Initialize[InputTask[ClientJobParams]] = Def.inputTask {
    val state = Keys.state.value
    val args = spaceDelimited().parsed
    val mainClass = (Keys.run / Keys.mainClass).value
    val service = bgJobService.value
    val fo = (Keys.run / Keys.forkOptions).value
    val workingDir = service.createWorkingDirectory
    val cp = service.copyClasspath(
      exportedProductJars.value,
      fullClasspathAsJars.value,
      workingDir,
      hashContents = true,
    )
    val strategy = fo.outputStrategy.map(_.getClass().getSimpleName().filter(_ != '$'))
    // sbtn doesn't set java.home, so we need to do the fallback here
    val javaHome =
      fo.javaHome.map(IO.toURI).orElse(sys.props.get("java.home").map(x => IO.toURI(new File(x))))
    val jvmRunInfo = JvmRunInfo(
      args = args.toVector,
      classpath = cp.map(x => IO.toURI(x.data)).map(FilePath(_, "")).toVector,
      mainClass = mainClass.getOrElse(sys.error("no main class")),
      connectInput = fo.connectInput,
      javaHome = javaHome,
      outputStrategy = strategy,
      workingDirectory = fo.workingDirectory.map(IO.toURI),
      jvmOptions = fo.runJVMOptions,
      environmentVariables = fo.envVars.toMap,
    )
    val info = RunInfo(
      jvm = true,
      jvmRunInfo = jvmRunInfo,
    )
    val result = ClientJobParams(
      runInfo = info
    )
    import sbt.internal.worker.codec.JsonProtocol._
    state.notifyEvent(Serialization.clientJob, result)
    result
  }
}
