package memeid.bits

import scala.annotation.tailrec

import cats.instances.long._
import cats.syntax.eq._

/*
 *  A module to work with with bitwise operations on `Long` values.
 *
 *  The JVM Long type is represented as a 64-bit signed two's complement integer.
 *
 *  This is not general-purpose, should not be used outside of the `memeid` implementation since it makes assumptions about the inputs it receives that can't be enforced by the type system.
 *
 */
object Bits {
  private def shiftLeft(x: Long, offset: Long) = x << offset

  private def setBit(x: Long, n: Long): Long = x | (1L << n)

  private def expt2(pow: Long): Long = setBit(0, pow)

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
  private def maskWidthRec(mask: Long, res: Long): Long = {
    val remaining = 1 & (mask >> res)
    if (remaining === 0) {
      res
    } else {
      maskWidthRec(mask, res + 1)
    }
  }

  /* Given a bitmask, retrieve its `offset`. */
  def maskOffset(mask: Long): Long = {
    if (mask === 0) {
      0
    } else if (mask < 0) {
      64 - maskWidth(mask)
    } else {
      maskOffsetRec(mask, 0)
    }
  }

  @tailrec
  private def maskOffsetRec(mask: Long, res: Long): Long = {
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
  protected[memeid] def fromBytes(barr: Seq[Byte]): Long =
    fromBytesRec(0, barr, 8)

  @SuppressWarnings(Array("scalafix:DisableSyntax.==", "scalafix:Disable.head"))
  @tailrec
  private def fromBytesRec(result: Long, bytes: Seq[Byte], count: Int): Long = {
    if (count == 0)
      result
    else
      fromBytesRec(
        Bits.writeByte(
          Bits.mask(8, 8L * (count - 1)),
          result,
          bytes.head.toLong
        ),
        bytes.tail,
        count - 1
      )
  }

  /* Convert to a byte array from a Long. */
  protected[memeid] def toBytes(x: Long): Array[Byte] =
    toBytesRec(x, new Array[Byte](8), 7, 0)

  @tailrec
  private def toBytesRec(x: Long, bytes: Array[Byte], b: Int, key: Int): Array[Byte] = {
    if (b < 0)
      bytes
    else {
      val read = Bits.readByte(Bits.mask(8, 8L * b), x)
      bytes(key) = read.toByte
      toBytesRec(x, bytes, b - 1, key + 1)
    }
  }

  object Cast {
    def sb8(b: Long): Byte = (0x00000000000000ff & b).toByte
  }
}
