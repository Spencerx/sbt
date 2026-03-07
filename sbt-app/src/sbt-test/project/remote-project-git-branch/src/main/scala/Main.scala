object Main {
  def main(args: Array[String]): Unit = {
    val cls = Class.forName("upstream.Greeter")
    val value = cls.getMethod("greet").invoke(null)
    assert(value == "hello-from-feature", s"Expected 'hello-from-feature' but got '$value'")
    println(s"Greeter loaded: $value")
  }
}
