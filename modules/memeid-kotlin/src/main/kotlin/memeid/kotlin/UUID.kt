/*
 * Copyright 2019-2020 47 Degrees Open Source <https://www.47deg.com>
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

package memeid.kotlin

import memeid.kotlin.node.Node
import memeid.kotlin.time.time
import memeid.UUID
import memeid.kotlin.digest.Digestible
import memeid.kotlin.time.posix

sealed class UUID {

  object V1 {
    val next: UUID = UUID.V1.next(Node.node) { time.monotonic }
  }

  object V3 {
    /**
     * Construct a namespace name-based v3 UUID. Uses MD5 as a hash algorithm
     *
     * @param namespace [UUID] used for the [V3] generation
     * @param name [String] used for the [V3] generation
     * @return [V3]
     */
    operator fun invoke(
      namespace: UUID,
      name: String
    ): UUID = UUID.V3.from(namespace, name, Digestible(name)::toByteArray.call())

    /**
     * Construct a namespace name-based v3 UUID. Uses MD5 as a hash algorithm
     *
     * @param namespace [UUID] used for the [V3] generation
     * @param name [UUID] used for the [V3] generation
     * @return [V3]
     */
    operator fun invoke(
      namespace: UUID,
      name: UUID
    ): UUID = UUID.V3.from(namespace, name, Digestible(name)::toByteArray.call())

    /**
     * Construct a namespace name-based v3 UUID. Uses MD5 as a hash algorithm
     *
     * @param namespace [UUID] used for the [V3] generation
     * @param custom [A] used for the [V3] generation
     * @param function ([A]) -> [ByteArray] customizes digestible toByteArray
     * @return [V3]
     */
    inline operator fun <reified A> invoke(
      namespace: UUID,
      custom: A,
      noinline function: (A) -> ByteArray
    ): UUID = UUID.V3.from(namespace, custom, Digestible(custom, function)::toByteArray.call())
  }

  object V4 {

    /**
     * Construct a v4 (random) UUID from the given `msb` and `lsb`.
     *
     * @param msb Most significant bit in [Long] representation
     * @param lsb Least significant bit in [Long] representation
     * @return [V4]
     */
    operator fun invoke(msb: Long, lsb: Long): UUID = UUID.V4.from(msb, lsb)

    // Construct a v4 (random) UUID.
    val random: UUID = UUID.V4.random()

    // Construct a SQUUID (random, time-based) UUID.
    val squuid: UUID = UUID.V4.squuid(posix.value)
  }

  object V5 {

    /**
     * Construct a namespace name-based v5 UUID. Uses SHA as a hash algorithm
     *
     * @param namespace [UUID] used for the [V5] generation
     * @param name [UUID] used for the [V5] generation
     * @return [V5]
     */
    operator fun invoke(
      namespace: UUID,
      name: String
    ): UUID = UUID.V5.from(namespace, name, Digestible(name)::toByteArray.call())

    /**
     * Construct a namespace name-based v5 UUID. Uses SHA as a hash algorithm
     *
     * @param namespace [UUID] used for the [V5] generation
     * @param name [UUID] used for the [V5] generation
     * @return [V5]
     */
    operator fun invoke(
      namespace: UUID,
      name: UUID
    ): UUID = UUID.V5.from(namespace, name, Digestible(name)::toByteArray.call())

    /**
     * Construct a namespace name-based v5 UUID. Uses SHA as a hash algorithm
     *
     * @param namespace [UUID] used for the [V5] generation
     * @param custom [A] used for the [V5] generation
     * @param function ([A]) -> [ByteArray] customizes digestible toByteArray
     * @return [V5]
     */
    inline operator fun <reified A> invoke(
      namespace: UUID,
      custom: A,
      noinline function: (A) -> ByteArray
    ): UUID = UUID.V5.from(namespace, custom, Digestible(custom, function)::toByteArray.call())
  }

  companion object {
    /**
     * Creates a valid [UUID] from two [kotlin.Long] values representing
     * the most/least significant bits.
     *
     * @param msb Most significant bit in [kotlin.Long] representation
     * @param lsb Least significant bit in [kotlin.Long] representation
     * @return a new [UUID] constructed from msb and lsb
     */
    fun from(msb: Long, lsb: Long): UUID = UUID.from(msb, lsb)

    /**
     * Creates a valid [UUID] from a [[UUID]].
     *
     * @param jUuid the [java.util.UUID]
     * @return a valid [java.util.UUID]
     */
    fun fromUUID(jUuid: java.util.UUID): UUID = UUID.fromUUID(jUuid)

    /**
     * Creates a [memeid.UUID] from the [java.util.UUID]::toString string standard
     * representation wrapped in an [Optional].
     *
     * Returns [None] with the error in case the string doesn't follow the
     * string standard representation.
     *
     * @param str String for the [memeid.UUID] to be generated as an [memeid.UUID]
     * @return [Some] with the error in case the string doesn't follow the
     *          string standard representation or [None] with the [MEME_ID]
     *          representation.
     */
    fun fromString(str: String): UUID = try {
      UUID.fromString(str)
    } catch (e: IllegalArgumentException) {
      UUID.NIL
    }
  }
}


