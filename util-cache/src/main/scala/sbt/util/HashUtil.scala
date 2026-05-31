package sbt.util

import java.nio.file.Path as NioPath
import sbt.internal.util.hashing.Hashing

object HashUtil:
  private[sbt] def xxhash64(bytes: Array[Byte]): Long =
    Hashing.xxhash64(0L).hash(bytes, 0, bytes.size)

  private[sbt] def imohash64(path: NioPath): Long =
    val hash64 = Hashing.samplingFileHashWyHash64(0)
    hash64.hash(path)

  private[sbt] def imohash64Str(path: NioPath): String =
    "imoxx64-" + imohash64(path).toHexString
end HashUtil
