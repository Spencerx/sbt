import upstream.{ Greeter, Helper }

object Main {
  def main(args: Array[String]): Unit = {
    val _ = (Greeter.greet, Helper.help)
  }
}
