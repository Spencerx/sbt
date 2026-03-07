lazy val dep = RootProject(uri(s"git:file://${System.getProperty("user.dir")}/upstream-repo"))

// Uses the default Manual strategy
lazy val root = (project in file(".")).dependsOn(dep)
