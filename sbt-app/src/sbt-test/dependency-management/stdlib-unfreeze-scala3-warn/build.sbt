// Same as stdlib-unfreeze-scala3-eviction but with allowUnsafeScalaLibUpgrade := true on low.
// low has scalaVersion 3.3.2 and depends on high (3.3.4); we demote the eviction to a warning so compile succeeds.

lazy val high = project.settings(
  scalaVersion := "3.3.4",
)

lazy val low = project.dependsOn(high).settings(
  allowUnsafeScalaLibUpgrade := true,
  scalaVersion := "3.3.2",
)
