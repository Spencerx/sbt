ThisBuild / scalaVersion := "2.12.20"

lazy val root = (project in file(".")).
  settings(
    incOptions := Def.uncached(xsbti.compile.IncOptions.of())
  )
