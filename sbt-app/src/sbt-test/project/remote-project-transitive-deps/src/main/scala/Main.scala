import upstream.{ AService, BService }

object Main {
  def main(args: Array[String]): Unit = {
    val _ = (AService.provide, BService.provide)
  }
}
