/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 *
 */

package sbt.internal.util.hashing

import java.lang.invoke.{ MethodHandles, VarHandle }
import java.nio.{ ByteBuffer, ByteOrder }

sealed trait Access[A1]:
  def readByte(a: A1, off: Int): Byte
  def readIntLE(a: A1, off: Int): Int
  def readLongLE(a: A1, off: Int): Long
end Access

object Access:
  private def getArrayClass(c: Class[?]): Class[?] =
    java.lang.reflect.Array.newInstance(c, 0).getClass
  private val LONG_HANDLE: VarHandle =
    MethodHandles.byteArrayViewVarHandle(getArrayClass(classOf[Long]), ByteOrder.LITTLE_ENDIAN)
  private val INT_HANDLE: VarHandle =
    MethodHandles.byteArrayViewVarHandle(getArrayClass(classOf[Int]), ByteOrder.LITTLE_ENDIAN)
  private val BB_LONG_HANDLE: VarHandle =
    MethodHandles.byteBufferViewVarHandle(getArrayClass(classOf[Long]), ByteOrder.LITTLE_ENDIAN)
  private val BB_INT_HANDLE: VarHandle =
    MethodHandles.byteBufferViewVarHandle(getArrayClass(classOf[Int]), ByteOrder.LITTLE_ENDIAN)

  given Access[Array[Byte]]:
    inline def readByte(buf: Array[Byte], off: Int): Byte =
      buf(off)
    inline def readIntLE(buf: Array[Byte], off: Int): Int =
      INT_HANDLE.get(buf, off).asInstanceOf[Int]
    inline def readLongLE(buf: Array[Byte], off: Int): Long =
      LONG_HANDLE.get(buf, off).asInstanceOf[Long]

  given Access[ByteBuffer]:
    inline def readByte(buf: ByteBuffer, off: Int): Byte =
      buf.get(off)
    inline def readIntLE(buf: ByteBuffer, off: Int): Int =
      assert(buf.order() == ByteOrder.LITTLE_ENDIAN)
      BB_INT_HANDLE.get(buf, off).asInstanceOf[Int]
    inline def readLongLE(buf: ByteBuffer, off: Int): Long =
      assert(buf.order() == ByteOrder.LITTLE_ENDIAN)
      BB_LONG_HANDLE.get(buf, off).asInstanceOf[Long]
end Access
