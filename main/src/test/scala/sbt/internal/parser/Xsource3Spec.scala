/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 */

package sbt
package internal
package parser

import java.io.File

import scala.io.Source

object Xsource3Spec extends AbstractSpec {
  implicit val splitter: SplitExpressions.SplitExpression = EvaluateConfigurations.splitExpressions

  test("Parser should handle -Xsource:3 syntax") {
    val rootPath = getClass.getResource("/xsource3").getPath
    println(s"Reading files from: $rootPath")
    val allFiles = new File(rootPath).listFiles.toList
    allFiles foreach { path =>
      println(s"$path")
      val lines = Source.fromFile(path).getLines().toList
      val (_, statements) = splitter(path, lines)
      assert(statements.nonEmpty, s"""
           |***should contains statements***
           |$lines """.stripMargin)
    }
  }
}
