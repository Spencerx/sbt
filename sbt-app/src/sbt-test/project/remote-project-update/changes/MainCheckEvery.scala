import upstream.{ Greeter, Helper, Utils, ManualClass, EveryClass }

object Main {
  def main(args: Array[String]): Unit = {
    val _ = (Greeter.greet, Helper.help, Utils.util, ManualClass.value, EveryClass.value)
  }
}
