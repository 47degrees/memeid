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

import memeid.bits._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class BitsSpec extends Specification with ScalaCheck {

  "Bits.mask" should {
    "construct a bitmask correctly" in {
      mask(0, 0) must be equalTo (0)
      mask(0, 1) must be equalTo (0)
      mask(1, 0) must be equalTo (1)
      mask(2, 0) must be equalTo (3)
      mask(4, 0) must be equalTo (0xF)
      mask(8, 0) must be equalTo (0xFF)
      mask(16, 0) must be equalTo (0xFFFF)
      mask(7, 0) must be equalTo (0x7F)
      mask(8, 8) must be equalTo (0xFF00)
      mask(8, 4) must be equalTo (0xFF0)
      mask(64, 0) must be equalTo (-1)
      mask(63, 1) must be equalTo (-2)
      mask(60, 4) must be equalTo (-16)
      mask(32, 0) must be equalTo (0x0000000FFFFFFFFL)
      mask(32, 16) must be equalTo (0x000FFFFFFFF0000L)
      mask(32, 32) must be equalTo (-4294967296L)
      mask(3, 60) must be equalTo (8070450532247928832L)
      mask(3, 61) must be equalTo (-2305843009213693952L)
      mask(4, 60) must be equalTo (-1152921504606846976L)
      mask(8, 48) must be equalTo (71776119061217280L)
      mask(16, 48) must be equalTo (-281474976710656L)
    }

    "bitmasks can be OR-ed" in {
      mask(2, 0) must be equalTo (mask(1, 1) | mask(1, 0))
      mask(4, 0) must be equalTo (mask(2, 2) | mask(2, 0))
      mask(8, 0) must be equalTo (mask(4, 4) | mask(4, 0))
      mask(8, 0) must be equalTo (mask(2, 6) | mask(6, 0))
      mask(8, 0) must be equalTo (mask(1, 7) | mask(7, 0))
      mask(16, 0) must be equalTo (mask(8, 8) | mask(8, 0))
      mask(16, 0) must be equalTo (mask(15, 1) | mask(1, 0))
      mask(16, 0) must be equalTo (mask(5, 11) | mask(11, 0))
    }

    "mask width can be queried" in prop { (width: Long, offset: Long) =>
      ((width > 0) && (offset >= 0) && (width <= 64) && (offset <= 64) && ((width + offset) <= 64)) ==> {
        val m = mask(width, offset)
        maskWidth(m) must be equalTo (width)
      }
    }.set(minTestsOk = 25, maxDiscardRatio = 100)

    "mask offset can be queried" in prop { (width: Long, offset: Long) =>
      ((width > 0) && (offset >= 0) && (width <= 64) && (offset <= 64) && ((width + offset) <= 64)) ==> {
        val m = mask(width, offset)
        maskOffset(m) must be equalTo (offset)
      }
    }.set(minTestsOk = 25, maxDiscardRatio = 100)

  }

  "Bits.readByte" should {
    "read the bytes specified by the bitmask" in {
      readByte(mask(4, 0), 0x0000000FFFFFFFFL) must be equalTo (0xF)
      readByte(mask(4, 8), 0x0000000FFFFFFFFL) must be equalTo (0xF)
      readByte(mask(4, 0), mask(32, 2)) must be equalTo (0xC)
      readByte(mask(64, 0), 0xFFFFFFFFFFFFFFFFL) must be equalTo (-1)
      readByte(mask(63, 1), 0xFFFFFFFFFFFFFFFFL) must be equalTo (0x7FFFFFFFFFFFFFFFL)
      readByte(mask(60, 4), 0xFFFFFFFFFFFFFFFFL) must be equalTo (0x0FFFFFFFFFFFFFFFL)
      readByte(mask(1, 63), 0xFFFFFFFFFFFFFFFFL) must be equalTo (0x1)
      readByte(mask(4, 60), 0xFFFFFFFFFFFFFFFFL) must be equalTo (0xF)
      readByte(mask(4, 60), 0x7FFFFFFFFFFFFFFFL) must be equalTo (7)
    }

    "bit access" in {
      (0L to 63L)
        .map(i => readByte(mask(1, i), 0xFFFFFFFFFFFFFFFFL))
        .toSet must be equalTo (Set(1))
    }

    "nibble access" in {
      (0L to 60L)
        .map(i => readByte(mask(4, i), 0xFFFFFFFFFFFFFFFFL))
        .toSet must be equalTo (Set(0xF))
    }
  }

  "Bits.writeByte" should {
    "allows us to put bytes" in {
      val byte = 0x3L
      val full = 0xFFFFFFFFFFFFFFFFL

      (0L to 7L).map { i =>
        val m       = mask(4, i * 4)
        val written = writeByte(m, full, byte)
        readByte(m, written)
      }.toSet must be equalTo (Set(byte))
    }
  }

  "Bits.Cast" should {
    "work for significant 8-bit values" in {
      Cast.sb8(255) must be equalTo (-1)
      Cast.sb8(127) must be equalTo (127)
      Cast.sb8(-128) must be equalTo (-128)
      Cast.sb8(-254) must be equalTo (2)
    }
  }

  "Bits.fromByte and Bits.toByte" should {
    "round-trip" in {
      val bytes =
        (0 to 7).map(_ => Cast.sb8(new scala.util.Random().nextInt(mask(8, 0).toInt).toLong))
      val assembled    = fromBytes(bytes)
      val disassembled = toBytes(assembled)
      bytes.toList must be equalTo (disassembled.toList)
    }
  }

}
