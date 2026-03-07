object Main {
  def main(args: Array[String]): Unit = {
    val cls = Class.forName("upstream.Service")
    val value = cls.getMethod("provide").invoke(null)
    assert(value == "service", s"Unexpected Service.provide: '$value'")
    println(s"Service loaded: $value")
  }
}
