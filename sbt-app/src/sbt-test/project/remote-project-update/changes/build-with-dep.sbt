lazy val dep = RootProject(uri(s"git:file://${System.getProperty("user.dir")}/upstream-repo"))

lazy val root = (project in file(".")).dependsOn(dep).settings(
  dep / repositoryUpdateStrategy := RepositoryUpdateStrategy.Always
)
