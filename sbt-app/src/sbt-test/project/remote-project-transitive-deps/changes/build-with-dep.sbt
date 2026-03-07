lazy val aProject = ProjectRef(
  uri(s"git:file://${System.getProperty("user.dir")}/upstream-repo"),
  "a"
)

lazy val root = (project in file(".")).dependsOn(aProject)
