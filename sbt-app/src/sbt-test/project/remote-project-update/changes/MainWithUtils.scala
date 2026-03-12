import upstream.{ Greeter, Helper, Utils }

object Main {
  def main(args: Array[String]): Unit = {
    val _ = (Greeter.greet, Helper.help, Utils.util)
  }
}
