/*
 * Copyright 2019-2020 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package memeid

import scala.annotation.tailrec

/*
 *  A module to work with with bitwise operations on `Long` values.
 *
 *  The JVM Long type is represented as a 64-bit signed two's complement integer.
 *
 *  This is not general-purpose, should not be used outside of the `memeid` implementation since it makes assumptions about the inputs it receives that can't be enforced by the type system.
 *
 */
private[memeid] object bits {
  def shiftLeft(x: Long, offset: Long): Long = x << offset

  def setBit(x: Long, n: Long): Long = x | (1L << n)

  def expt2(pow: Long): Long = setBit(0, pow)

  /* Create a bitmask of the given `width` and `offset`. */
  def mask(width: Long, offset: Long): Long = {
    val sum = width + offset
    if (sum < 64) {
      shiftLeft(shiftLeft(1, width) - 1, offset)
    } else {
      val x = expt2(offset)
      -1L & ~(x - 1)
    }
  }

  /* Given a bitmask, retrieve its `width`. */
  def maskWidth(mask: Long): Long = {
    if (mask < 0) {
      64 - maskWidth(-(mask + 1))
    } else {
      val m = mask >> maskOffset(mask)
      maskWidthRec(m, 0)
    }
  }

  @tailrec
  def maskWidthRec(mask: Long, res: Long): Long = { 1 & (mask >> res) } match {
    case 0 => res
    case _ => maskWidthRec(mask, res + 1)
  }

  /* Given a bitmask, retrieve its `offset`. */
  def maskOffset(mask: Long): Long = mask match {
    case 0          => 0
    case n if n < 0 => 64 - maskWidth(n)
    case n          => maskOffsetRec(n, 0)
  }

  @tailrec
  def maskOffsetRec(mask: Long, res: Long): Long = {
    val firstBit = 1L & (mask >> res)
    if (firstBit > 0) {
      res
    } else {
      maskOffsetRec(mask, res + 1)
    }
  }

  /* Read the byte(s) from `num` specified by the `bitmask`. */
  def readByte(bitmask: Long, num: Long): Long = {
    val off = maskOffset(bitmask)
    (bitmask >>> off) & (num >>> off)
  }

  /* Write the byte(s) from `value` in `num` using the given `bitmask`. */
  def writeByte(bitmask: Long, num: Long, value: Long): Long = {
    val off     = maskOffset(bitmask)
    val shifted = shiftLeft(value, off)
    (num & ~bitmask) | (bitmask & shifted)
  }

  /* Convert to a Long from a byte seq. */
  def fromBytes(barr: Seq[Byte]): Long =
    fromBytesRec(0, barr, 8)

  @SuppressWarnings(Array("scalafix:Disable.head"))
  @tailrec
  def fromBytesRec(result: Long, bytes: Seq[Byte], count: Int): Long = count match {
    case 0 => result
    case _ =>
      fromBytesRec(
        writeByte(
          mask(8, 8L * (count - 1)),
          result,
          bytes.head.toLong
        ),
        bytes.tail,
        count - 1
      )
  }

  /* Convert to a byte array from a Long. */
  def toBytes(x: Long): Array[Byte] =
    toBytesRec(x, new Array[Byte](8), 7, 0)

  @tailrec
  def toBytesRec(x: Long, bytes: Array[Byte], b: Int, key: Int): Array[Byte] = {
    if (b < 0)
      bytes
    else {
      val read = readByte(mask(8, 8L * b), x)
      bytes(key) = read.toByte
      toBytesRec(x, bytes, b - 1, key + 1)
    }
  }

}
