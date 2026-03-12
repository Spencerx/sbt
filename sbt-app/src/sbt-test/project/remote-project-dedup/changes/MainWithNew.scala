import upstream.{ Service, NewService }

object Main {
  def main(args: Array[String]): Unit = {
    val _ = (Service.provide, NewService.provide)
  }
}
