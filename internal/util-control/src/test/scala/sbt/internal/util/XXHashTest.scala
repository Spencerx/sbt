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

object XXHashByteArrayTest extends AbstractHashTest:
  override val hash64: HashAlgo[Array[Byte]] = Hashing.xxhash64(0L)
  override def newStreaming(seed: Int): StreamingHashAlgo =
    Hashing.newStreamingXXHash64(seed)
  override val emptyHash = -1205034819632174695L
  override val zeroHash = -1642502924627794072L
end XXHashByteArrayTest

object XXHashByteBufferHashTest extends AbstractByteBufferHashTest:
  override val hash64: HashAlgo[ByteBuffer] =
    Hashing.xxhash64ByteBuffer(0L)
  override val emptyHash = -1205034819632174695L
  override val zeroHash = -1642502924627794072L
end XXHashByteBufferHashTest
