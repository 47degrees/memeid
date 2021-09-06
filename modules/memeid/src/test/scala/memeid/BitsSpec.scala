/*
 * Copyright 2019-2021 47 Degrees Open Source <https://www.47deg.com>
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

import scala.util.Random

import memeid.Bits._
import org.specs2.mutable.Specification

class BitsSpec extends Specification {

  "Bits.readByte" should {
    "read the bytes specified by the bitmask" in {
      (readByte(mask(4, 0), 0x0000000ffffffffL) must be).equalTo(0xf)
      (readByte(mask(4, 8), 0x0000000ffffffffL) must be).equalTo(0xf)
      (readByte(mask(4, 0), mask(32, 2)) must be).equalTo(0xc)
      (readByte(mask(64, 0), 0xffffffffffffffffL) must be).equalTo(-1)
      (readByte(mask(63, 1), 0xffffffffffffffffL) must be).equalTo(0x7fffffffffffffffL)
      (readByte(mask(60, 4), 0xffffffffffffffffL) must be).equalTo(0x0fffffffffffffffL)
      (readByte(mask(1, 63), 0xffffffffffffffffL) must be).equalTo(0x1)
      (readByte(mask(4, 60), 0xffffffffffffffffL) must be).equalTo(0xf)
      (readByte(mask(4, 60), 0x7fffffffffffffffL) must be).equalTo(7)
    }

    "bit access" in {
      ((0L to 63L)
        .map(i => readByte(mask(1, i), 0xffffffffffffffffL))
        .toSet must be).equalTo(Set(1))
    }

    "nibble access" in {
      ((0L to 60L)
        .map(i => readByte(mask(4, i), 0xffffffffffffffffL))
        .toSet must be).equalTo(Set(0xf))
    }
  }

  "Bits.writeByte" should {
    "allows us to put bytes" in {
      val byte = 0x3L
      val full = 0xffffffffffffffffL

      ((0L to 7L).map { i =>
        val m       = mask(4, i * 4)
        val written = writeByte(m, full, byte)
        readByte(m, written)
      }.toSet must be).equalTo(Set(byte))
    }
  }

  "Bits.Cast" should {
    "work for significant 8-bit values" in {
      (Cast.sb8(255) must be).equalTo(-1)
      (Cast.sb8(127) must be).equalTo(127)
      (Cast.sb8(-128) must be).equalTo(-128)
      (Cast.sb8(-254) must be).equalTo(2)
    }
  }

  "Bits.fromByte and Bits.toByte" should {
    "round-trip" in {
      val bytes =
        (0 to 7)
          .map(_ => Cast.sb8(new Random().nextInt(mask(8, 0).toInt).toLong))
          .toArray
      val assembled    = fromBytes(bytes)
      val disassembled = toBytes(assembled)
      (bytes.toList must be).equalTo(disassembled.toList)
    }
  }

  object Cast {

    def sb8(b: Long): Byte = (0x00000000000000ff & b).toByte

  }

  private def mask(width: Long, offset: Long): Long =
    if (width + offset < 64L) (1L << width) - 1L << offset else -(1L << offset)

}
