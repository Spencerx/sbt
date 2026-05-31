/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 *
 */

package sbt.internal.util.hashing

import java.nio.ByteBuffer

object Hashing:
  def xxhash64(seed: Long): HashAlgo[Array[Byte]] =
    XXHash64.byteArray(seed)

  def xxhash64ByteBuffer(seed: Long): HashAlgo[ByteBuffer] =
    XXHash64.byteBuffer(seed)

  def wyhash64(seed: Long): HashAlgo[Array[Byte]] =
    WyHash64.byteArray(seed)

  def wyhash64ByteBuffer(seed: Long): HashAlgo[ByteBuffer] =
    WyHash64.byteBuffer(seed)

  def newStreamingXXHash64(seed: Long): StreamingHashAlgo =
    new StreamingXXHash64VarHandle(seed)

  def newStreamingWyHash64(seed: Long): StreamingHashAlgo =
    new StreamingWyHash64VarHandle(seed)

  def samplingFileHashXXHash64(seed: Long): FileHash =
    FileSampleHash(newStreamingXXHash64(seed))

  def samplingFileHashWyHash64(seed: Long): FileHash =
    FileSampleHash(newStreamingWyHash64(seed))
end Hashing
