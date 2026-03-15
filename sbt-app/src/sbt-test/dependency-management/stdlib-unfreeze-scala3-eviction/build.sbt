// Regression test for #6694: Scala 3 eviction error when scalaVersion < scala3-library_3 on classpath.
// low has scalaVersion 3.3.2 and depends on high (3.3.4), so resolved scala3-library_3 is 3.3.4.
// Without allowUnsafeScalaLibUpgrade, compile must fail with the eviction error.

lazy val high = project.settings(
  scalaVersion := "3.3.4",
)

lazy val low = project.dependsOn(high).settings(
  scalaVersion := "3.3.2",
  // do NOT set allowUnsafeScalaLibUpgrade — we expect the build to fail
)
