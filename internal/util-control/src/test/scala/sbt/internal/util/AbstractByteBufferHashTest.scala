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
import verify.BasicTestSuite

abstract class AbstractByteBufferHashTest extends BasicTestSuite:
  def hash64: HashAlgo[ByteBuffer]
  def emptyHash: Long
  def zeroHash: Long

  test("Hash empty ByteBuffer"):
    val buf: ByteBuffer = ByteBuffer.allocate(0)
    val r = hash64.hash(buf, 0, 0)
    assert(r == emptyHash)

  test("Hash one byte ByteBuffer"):
    val buf: ByteBuffer = ByteBuffer.allocate(1)
    buf.put(0: Byte)
    buf.rewind()
    val r = hash64.hash(buf, 0, 1)
    assert(r == zeroHash)
end AbstractByteBufferHashTest
