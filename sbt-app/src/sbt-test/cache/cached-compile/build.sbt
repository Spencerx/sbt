import sbt.internal.util.CacheEventSummary

lazy val checkMiss = taskKey[Unit]("")

Global / localCacheDirectory := baseDirectory.value / "diskcache"

scalaVersion := "3.7.0"
checkMiss := {
  val s = streams.value
  val config = Def.cacheConfiguration.value
  val prev = config.cacheEventLog.previous match
    case s: CacheEventSummary.Data => s
    case _                         => sys.error(s"empty event log")
  s.log.info(prev.missCount.toString) 
  assert(prev.missCount == 2, s"prev.missCount = ${prev.missCount}")
}
