/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 *
 */

package sbt.internal.util.hashing

object FarmHashConstants:
  final val K0 = 0xc3a5c85c97cb3127L
  final val K1 = 0xb492b66fbe98f273L
  final val K2 = 0x9ae16a3b2f90404fL
  final val K_MUL = 0x9ddfea08eb382d69L
end FarmHashConstants
