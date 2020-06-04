package memeid.kotlin

import memeid.kotlin.UUID  as K_UUID
import memeid.UUID as MEME_ID
import java.util.UUID as J_UUID

/** The most significant 64 bits of this UUID's 128 bit value */
val MEME_ID.msb: Long
  get() = this.mostSignificantBits

/** The least significant 64 bits of this UUID's 128 bit value */
val MEME_ID.lsb: Long
  get() = this.leastSignificantBits

/**
 * Returns this UUID as the provided type if versions match;
 * otherwise, returns [None].
 *
 * @tparam A to be sup-typed [memeid.kotlin.UUID]
 * @return this [memeid.kotlin.UUID] as the provided type if versions match;
 *         otherwise, returns [None]
 */
inline fun <reified A> K_UUID.getOptional(uuid: A?): Optional<A> = if (uuid is A) Some(uuid) else None()

// fun K_UUID.unapply(str: String): Optional<K_UUID> = from(str).getOptional(UUID) else None

/**
 * Creates a valid [memeid.kotlin.UUID] from two [kotlin.Long] values representing
 * the most/least significant bits.
 *
 * @param msb Most significant bit in [kotlin.Long] representation
 * @param lsb Least significant bit in [kotlin.Long] representation
 * @return a new [memeid.kotlin.UUID] constructed from msb and lsb
 */
fun K_UUID.from(msb: Long, lsb: Long): K_UUID = from(msb, lsb)

/**
 * Creates a valid [UUID] from a [[UUID]].
 *
 * @param jUuid the [java.util.UUID]
 * @return a valid [java.util.UUID]
 */
fun K_UUID.fromUUID(jUuid: J_UUID): MEME_ID = MEME_ID.fromUUID(jUuid)

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
fun from(str: String): Optional<MEME_ID> = try {
  val result = MEME_ID.fromString(str)
  Some(result)
} catch (e: IllegalArgumentException) {
  println("$e") // TODO install Timber logging
  None()
}