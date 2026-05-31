/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 *
 */

package sbt.internal.util.hashing

import verify.BasicTestSuite

abstract class AbstractHashTest extends BasicTestSuite:
  def hash64: HashAlgo[Array[Byte]]
  def newStreaming(seed: Int): StreamingHashAlgo
  def emptyHash: Long
  def zeroHash: Long

  test("Hash empty array"):
    val buf: Array[Byte] = Array[Byte](0)
    val r = hash64.hash(buf, 0, 0)
    assert(r == emptyHash)

  test("Hash one byte array"):
    val buf: Array[Byte] = Array[Byte](0)
    val r = hash64.hash(buf, 0, 1)
    assert(r == zeroHash)

  test("Streaming one byte array"):
    val hash = newStreaming(0)
    try
      val buf: Array[Byte] = Array[Byte](0)
      hash.update(buf, 0, 1)
      assert(hash.getValue == zeroHash)
    finally hash.close()
end AbstractHashTest
