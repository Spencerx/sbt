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
import java.util.concurrent.ThreadLocalRandom
import net.openhft.hashing.LongHashFunction

object FarmHashTest extends BasicTestSuite:
  lazy val reference = LongHashFunction.farmNa()

  def hash64: HashAlgo[Array[Byte]] =
    Hashing.farmNaHash64
  def emptyHash: Long = -7286425919675154353L
  def zeroHash: Long = -4728684028706075820L

  test("Hash empty array"):
    val buf: Array[Byte] = new Array[Byte](0)
    val r = hash64.hash(buf, 0, 0)
    assert(r == emptyHash)
    val r2 = reference.hashBytes(buf)
    assert(r == r2)

  test("Hash one byte array"):
    val buf: Array[Byte] = Array[Byte](0)
    val r = hash64.hash(buf, 0, 1)
    assert(r == zeroHash)
    val r2 = reference.hashBytes(buf)
    assert(r == r2)

  test("Hash 2048 bytes"):
    val buf: Array[Byte] = new Array[Byte](2048)
    ThreadLocalRandom.current().nextBytes(buf)
    val r = hash64.hash(buf, 0, 2048)
    val r2 = reference.hashBytes(buf)
    assert(r == r2)
end FarmHashTest
