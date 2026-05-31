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

object WyHashByteArrayTest extends AbstractHashTest:
  override val hash64: HashAlgo[Array[Byte]] = Hashing.wyhash64(0L)
  override def newStreaming(seed: Int): StreamingHashAlgo =
    Hashing.newStreamingWyHash64(seed)
  override val emptyHash = 290873116282709081L
  override val zeroHash = -295637713410278011L
end WyHashByteArrayTest

object WyHasByteBufferHashTest extends AbstractByteBufferHashTest:
  override val hash64: HashAlgo[ByteBuffer] =
    Hashing.wyhash64ByteBuffer(0L)
  override val emptyHash = 290873116282709081L
  override val zeroHash = -295637713410278011L
end WyHasByteBufferHashTest
