lazy val a = RootProject(uri(s"git:file://${System.getProperty("user.dir")}/upstream-repo"))
lazy val b = (project in file("b")).dependsOn(a)
lazy val c = (project in file("c")).dependsOn(a)
lazy val d = (project in file("d")).dependsOn(b, c)

lazy val snapshotReflog = taskKey[Unit]("Records current HEAD reflog count in staging repo")
lazy val checkFetchCount = inputKey[Unit]("Asserts exactly N git resets happened since snapshot")

lazy val root = (project in file(".")).aggregate(b, c, d).settings(
  a / repositoryUpdateStrategy := RepositoryUpdateStrategy.Always,
  snapshotReflog := {
    val staging = baseDirectory.value / "global" / "staging"
    for {
      hashDir <- staging.listFiles() if hashDir.isDirectory
      repoDir <- hashDir.listFiles() if repoDir.isDirectory && (repoDir / ".git").exists()
    } {
      val headLog = repoDir / ".git" / "logs" / "HEAD"
      val count = if (headLog.exists()) IO.readLines(headLog).size else 0
      IO.write(repoDir / ".git" / "reflog-baseline", count.toString)
    }
  },
  checkFetchCount := {
    val expected = complete.Parsers.spaceDelimited("<expected>").parsed.head.toInt
    val staging = baseDirectory.value / "global" / "staging"
    val gitDirs = for {
      hashDir <- staging.listFiles() if hashDir.isDirectory
      repoDir <- hashDir.listFiles() if repoDir.isDirectory && (repoDir / ".git").exists()
    } yield repoDir
    assert(gitDirs.length == 1, s"Expected 1 staging repo, got ${gitDirs.length}")
    val repo = gitDirs.head
    val headLog = repo / ".git" / "logs" / "HEAD"
    val currentCount = if (headLog.exists()) IO.readLines(headLog).size else 0
    val baseline = repo / ".git" / "reflog-baseline"
    val beforeCount = if (baseline.exists()) IO.read(baseline).trim.toInt else 0
    val fetchCount = currentCount - beforeCount
    assert(fetchCount == expected, s"Expected fetch count $expected, got $fetchCount")
  }
)
