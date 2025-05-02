import sbt.internal.util.{ CacheEventSummary, StringVirtualFile1 }
import sjsonnew.BasicJsonProtocol.*

val pure1 = taskKey[Unit]("")
val map1 = taskKey[String]("")
val mapN1 = taskKey[Unit]("")
val checkMapN1 = taskKey[Unit]("")

Global / localCacheDirectory := baseDirectory.value / "diskcache"

pure1 := (Def.cachedTask {
  val output = StringVirtualFile1("target/out/a.txt", "foo")
  Def.declareOutput(output)
  ()
}).value

map1 := (Def.cachedTask {
  pure1.value
  val output1 = StringVirtualFile1("target/out/b1.txt", "foo")
  val output2 = StringVirtualFile1("target/out/b2.txt", "foo")
  Def.declareOutput(output1)
  Def.declareOutput(output2)
  "something"
}).value

mapN1 := (Def.cachedTask {
  pure1.value
  map1.value
  val output = StringVirtualFile1("target/out/c.txt", "foo")
  Def.declareOutput(output)
  ()
}).value

checkMapN1 := {
  val config = Def.cacheConfiguration.value
  val prev = config.cacheEventLog.previous match
    case s: CacheEventSummary.Data => s
    case s                         => sys.error(s"empty event log") 
  assert(prev.hitCount == 2, s"prev.hitCount = ${prev.hitCount}")
}
