object Main {
  def main(args: Array[String]): Unit = {
    val aClass = Class.forName("upstream.AService")
    val aValue = aClass.getMethod("provide").invoke(null)
    assert(aValue == "from-A-via-from-B", s"Unexpected AService.provide: '$aValue'")
    println(s"AService loaded: $aValue")

    val bClass = Class.forName("upstream.BService")
    val bValue = bClass.getMethod("provide").invoke(null)
    assert(bValue == "from-B", s"Unexpected BService.provide: '$bValue'")
    println(s"BService loaded: $bValue")
  }
}
