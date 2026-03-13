import upstream.{ Greeter, BranchOnly }

object Main {
  def main(args: Array[String]): Unit = {
    val _ = (Greeter.greet, BranchOnly.value)
  }
}
