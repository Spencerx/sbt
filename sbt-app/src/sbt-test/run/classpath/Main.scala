package t

import java.nio._, charset._, file._

object Main {
  def main(args: Array[String]): Unit = {
    println(Files.readString(Paths.get(getClass().getResource("/a.txt").toURI())))
  }
}