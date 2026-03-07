lazy val a = RootProject(uri(s"git:file://${System.getProperty("user.dir")}/upstream-repo"))
lazy val b = (project in file("b")).dependsOn(a)
lazy val c = (project in file("c")).dependsOn(a)
lazy val d = (project in file("d")).dependsOn(b, c)

lazy val root = (project in file(".")).aggregate(b, c, d).settings(
  a / repositoryUpdateStrategy := RepositoryUpdateStrategy.Always
)
