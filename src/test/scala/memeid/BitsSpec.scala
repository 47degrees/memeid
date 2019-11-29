package memeid.bits

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class BitsSpec extends Specification with ScalaCheck {
  "Bits.mask" should {
    "construct a bitmask correctly" in {
      Bits.mask(0, 0) must be equalTo (0)
      Bits.mask(0, 1) must be equalTo (0)
      Bits.mask(1, 0) must be equalTo (1)
      Bits.mask(2, 0) must be equalTo (3)
      Bits.mask(4, 0) must be equalTo (15)
      Bits.mask(8, 0) must be equalTo (255)
      Bits.mask(16, 0) must be equalTo (65535)
      Bits.mask(7, 0) must be equalTo (127)
      Bits.mask(8, 8) must be equalTo (65280)
      Bits.mask(8, 4) must be equalTo (4080)
      Bits.mask(64, 0) must be equalTo (-1)
      Bits.mask(63, 1) must be equalTo (-2)
      Bits.mask(60, 4) must be equalTo (-16)
      Bits.mask(32, 0) must be equalTo (4294967295L)
      Bits.mask(32, 16) must be equalTo (281474976645120L)
      Bits.mask(32, 32) must be equalTo (-4294967296L)
      Bits.mask(3, 60) must be equalTo (8070450532247928832L)
      Bits.mask(3, 61) must be equalTo (-2305843009213693952L)
      Bits.mask(4, 60) must be equalTo (-1152921504606846976L)
      Bits.mask(8, 48) must be equalTo (71776119061217280L)
      Bits.mask(16, 48) must be equalTo (-281474976710656L)
    }

    "bitmasks can be OR-ed" in {
      Bits.mask(2, 0) must be equalTo (Bits.mask(1, 1) | Bits.mask(1, 0))
      Bits.mask(4, 0) must be equalTo (Bits.mask(2, 2) | Bits.mask(2, 0))
      Bits.mask(8, 0) must be equalTo (Bits.mask(4, 4) | Bits.mask(4, 0))
      Bits.mask(8, 0) must be equalTo (Bits.mask(2, 6) | Bits.mask(6, 0))
      Bits.mask(8, 0) must be equalTo (Bits.mask(1, 7) | Bits.mask(7, 0))
      Bits.mask(16, 0) must be equalTo (Bits.mask(8, 8) | Bits.mask(8, 0))
      Bits.mask(16, 0) must be equalTo (Bits.mask(15, 1) | Bits.mask(1, 0))
      Bits.mask(16, 0) must be equalTo (Bits.mask(5, 11) | Bits.mask(11, 0))
    }

    // todo: use custom arbitrary/gen to create meaningful values instead of discarding so much
    "mask width can be queried" in prop { (width: Long, offset: Long) =>
      ((width > 0) && (offset >= 0) && (width <= 64) && (offset <= 64) && ((width + offset) <= 64)) ==> {
        val mask = Bits.mask(width, offset)
        Bits.maskWidth(mask) must be equalTo (width)
      }
    }.set(minTestsOk = 25, maxDiscardRatio = 100)

    "mask offset can be queried" in prop { (width: Long, offset: Long) =>
      ((width > 0) && (offset >= 0) && (width <= 64) && (offset <= 64) && ((width + offset) <= 64)) ==> {
        val mask = Bits.mask(width, offset)
        Bits.maskOffset(mask) must be equalTo (offset)
      }
    }.set(minTestsOk = 25, maxDiscardRatio = 100)

  }

  "Bits.loadByte" should {
    "load the bytes specified by the bitmask" in {
      Bits.loadByte(Bits.mask(4, 0), 0x0000000FFFFFFFFL) must be equalTo (0xF)
      Bits.loadByte(Bits.mask(4, 8), 0x0000000FFFFFFFFL) must be equalTo (0xF)
      Bits.loadByte(Bits.mask(4, 0), Bits.mask(32, 2)) must be equalTo (0xC)
      Bits.loadByte(Bits.mask(64, 0), 0xFFFFFFFFFFFFFFFFL) must be equalTo (-1)
      Bits.loadByte(Bits.mask(63, 1), 0xFFFFFFFFFFFFFFFFL) must be equalTo (0x7FFFFFFFFFFFFFFFL)
      Bits.loadByte(Bits.mask(60, 4), 0xFFFFFFFFFFFFFFFFL) must be equalTo (0x0FFFFFFFFFFFFFFFL)
      Bits.loadByte(Bits.mask(1, 63), 0xFFFFFFFFFFFFFFFFL) must be equalTo (0x1)
      Bits.loadByte(Bits.mask(4, 60), 0xFFFFFFFFFFFFFFFFL) must be equalTo (0xF)
      Bits.loadByte(Bits.mask(4, 60), 0x7FFFFFFFFFFFFFFFL) must be equalTo (7)
    }

    "bit access" in {
      (0L to 63L)
        .map(i => Bits.loadByte(Bits.mask(1, i), 0xFFFFFFFFFFFFFFFFL))
        .toSet must be equalTo (Set(1))
    }

    "nibble access" in {
      (0L to 60L)
        .map(i => Bits.loadByte(Bits.mask(4, i), 0xFFFFFFFFFFFFFFFFL))
        .toSet must be equalTo (Set(15))
    }
  }

  "Bits.depositByte" should {
    "allows us to put bytes" in {
      val byte = 0x3L
      val full = 0xFFFFFFFFFFFFFFFFL

      (0L to 7L)
        .map(i => {
          val mask      = Bits.mask(4, i * 4)
          val deposited = Bits.depositByte(mask, full, byte)
          Bits.loadByte(mask, deposited)
        })
        .toSet must be equalTo (Set(byte))
    }
  }
}
