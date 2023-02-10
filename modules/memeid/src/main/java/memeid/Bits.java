/*
 * Copyright 2019-2023 47 Degrees Open Source <https://www.47deg.com>
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

package memeid;

/*
 *  A module to work with with bitwise operations on `Long` values.
 *
 *  The JVM Long type is represented as a 64-bit signed two's complement integer.
 *
 *  This is not general-purpose, should not be used outside of the `memeid` implementation since it makes assumptions about the inputs it receives that can't be enforced by the type system.
 *
 */
public final class Bits {

	/* Convert to a byte array from a Long. */
	public static byte[] toBytes(final long x) {
		return new byte[] { (byte) (x >> 56), (byte) (x >> 48), (byte) (x >> 40), (byte) (x >> 32), (byte) (x >> 24),
				(byte) (x >> 16), (byte) (x >> 8), (byte) x };
	}

	/* Convert to a Long from a byte seq. */
	public static long fromBytes(final byte[] bytes) {
		assert bytes.length == 8 : "data must be 8 bytes in length";

		long l = 0;

		for (byte aByte : bytes) {
			l <<= 8;
			l |= aByte & 0xFF;
		}

		return l;
	}

	/* Read the byte(s) from `num` specified by the `bitmask`. */
	public static long readByte(final long bitmask, final long num) {
		final long off = maskOffset(bitmask);

		return (bitmask >>> off) & (num >>> off);
	}

	public static long readByte(final long bitmask, final long offset, final long num) {
		return (bitmask >>> offset) & (num >>> offset);
	}

	/* Write the byte(s) from `value` in `num` using the given `bitmask`. */
	public static long writeByte(final long bitmask, final long num, final long value) {
		return (num & ~bitmask) | (bitmask & value << maskOffset(bitmask));
	}

	/* Write the byte(s) from `value` in `num` using the given `bitmask`. */
	public static long writeByte(final long bitmask, final long offset, final long num, final long value) {
		return (num & ~bitmask) | (bitmask & value << offset);
	}

	/* Given a bitmask, retrieve its `offset`. */
	private static long maskOffset(long mask) {
		if (mask == 0 || mask == -1)
			return 0;
		else if (mask < 0) {
			final long next = -(mask + 1);

			long offset = offset(next);

			long res = 0;

			while ((1L & (next >> offset >> res)) != 0) {
				res++;
			}

			return 64 - (64 - res);
		} else
			return offset(mask);
	}

	private static long offset(long x) {
		long res = 0;

		while ((1L & (x >> res)) <= 0) {
			res++;
		}

		return res;
	}

}
