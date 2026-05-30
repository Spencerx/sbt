/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 *
 */

package sbt.internal.util.hashing

object XXHashTest extends AbstractHashTest:
  override val hash64: HashAlgo = Hashing.xxhash64
  override def newStreaming(seed: Int): StreamingHashAlgo =
    Hashing.newStreamingXXHash64(seed)
  override val emptyHash = -1205034819632174695L
  override val zeroHash = -1642502924627794072L
end XXHashTest
