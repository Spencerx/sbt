// 2.12.x uses Zinc's compiler bridge
ThisBuild / scalaVersion := "2.12.20"

@transient
lazy val check = taskKey[Unit]("")

check := {
  if (scala.util.Properties.isJavaAtLeast("25"))
    (Compile / run).toTask(" ").value
  else ()
}
