object Main {
  def main(args: Array[String]): Unit = {
    val greeterClass = Class.forName("upstream.Greeter")
    val greet = greeterClass.getMethod("greet").invoke(null)
    assert(greet == "hello", s"Expected 'hello' but got '$greet'")
    println(s"Greeter loaded: $greet")
  }
}
