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

import java.lang.Long.compareUnsigned
import java.util.{UUID => JUUID}

import scala.reflect.ClassTag
import scala.util.Try

import memeid.JavaConverters._
import memeid.bits.{fromBytes, mask, readByte, toBytes, writeByte}
import memeid.digest.{Algorithm, Digestible, MD5, SHA1}
import memeid.node.Node
import memeid.time.{Posix, Time}

/**
 * A class that represents an immutable universally unique identifier (UUID).
 * A UUID represents a 128-bit value.
 *
 * @see [[https://tools.ietf.org/html/rfc4122]]
 */
sealed trait UUID extends Comparable[UUID] {

  private[memeid] val juuid: JUUID

  /** The most significant 64 bits of this UUID's 128 bit value */
  @inline def msb: Long = juuid.getMostSignificantBits

  /** The least significant 64 bits of this UUID's 128 bit value */
  @inline def lsb: Long = juuid.getLeastSignificantBits

  /**
   * Returns this UUID as the provided type if versions match;
   * otherwise, returns `None`.
   *
   * @tparam A must be subtype of [[UUID]]
   * @return this [[UUID]] as the provided type if versions match;
   * otherwise, returns `None`
   */
  def as[A <: UUID: ClassTag]: Option[A] = this match {
    case a: A => Some(a)
    case _    => None
  }

  /**
   * Returns `true` if this UUID matches the provided type;
   * otherwise, returns `false`.
   *
   * @tparam A must be subtype of [[UUID]]
   * @return `true` if this [[UUID]] matches the provided type;
   * otherwise, returns `false`
   */
  def is[A <: UUID: ClassTag]: Boolean = this match {
    case _: A => true
    case _    => false
  }

  /**
   * The variant field determines the layout of the [[UUID]].
   *
   * The variant field consists of a variable number of
   * the most significant bits of octet 8 of the [[UUID]].
   *
   * The variant number has the following meaning:
   *
   * - '''0''': Reserved for NCS backward compatibility
   *
   * - '''2''': [[https://tools.ietf.org/html/rfc4122 IETF RFC 4122]]
   *
   * - '''6''': Reserved, Microsoft Corporation backward compatibility
   *
   * - '''7''': Reserved for future definition
   *
   * Interoperability, in any form, with variants other than the one
   * defined here is not guaranteed, and is not likely to be an issue in
   * practice.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.1]]
   * @return The variant of this [[UUID]]
   */
  @inline def variant: Int = juuid.variant

  /**
   * The version number associated with this [[UUID]].  The version
   * number describes how this [[UUID]] was generated.
   *
   * The version number has the following meaning:
   *
   * - '''1''': Time-based UUID
   *
   * - '''2''': DCE security UUID
   *
   * - '''3''': Name-based UUID
   *
   * - '''4''': Randomly generated UUID
   *
   * - '''5''': The name-based version that uses SHA-1 hashing
   *
   * @see https://tools.ietf.org/html/rfc4122#section-4.1.3
   * @return The version number of this [[UUID]]
   */
  @inline def version: Int = juuid.version

  @SuppressWarnings(Array("scalafix:Disable.equals", "scalafix:Disable.Any"))
  override def equals(obj: Any): Boolean = obj match {
    case x: UUID if compareTo(x).equals(0) => true
    case _                                 => false
  }

  override def compareTo(x: UUID): Int = {
    compareUnsigned(msb, x.msb) match {
      case 0     => compareUnsigned(lsb, x.lsb)
      case other => other
    }
  }

  @SuppressWarnings(Array("scalafix:Disable.hashCode"))
  override def hashCode(): Int = juuid.hashCode

  @SuppressWarnings(Array("scalafix:Disable.toString"))
  override def toString: String = juuid.toString

}

/**
 * Companion object for [[UUID]]
 */
object UUID {

  def unapply(str: String): Option[UUID] =
    if (!str.isEmpty) UUID.from(str).toOption
    else None

  /**
   * Creates a valid [[UUID]] from two [[scala.Long Long]] values representing
   * the most/least significant bits.
   * @param msb Most significant bit in [[scala.Long Long]] representation
   * @param lsb Least significant bit in [[scala.Long Long]] representation
   * @return a new [[UUID]] constructed from msb and lsb
   */
  def from(msb: Long, lsb: Long): UUID = new JUUID(msb, lsb).asScala

  /**
   * Creates a [[UUID UUID]] from the [[java.util.UUID#toString string standard representation]]
   * wrapped in a [[scala.util.Right Right]].
   *
   * Returns [[scala.util.Left Left]] with the error in case the string doesn't follow the
   * string standard representation.
   *
   * @param s String for the [[java.util.UUID UUID]] to be generated as an [[UUID]]
   * @return [[scala.util.Either Either]] with [[scala.util.Left Left]] with the error in case the string doesn't follow the
   *        string standard representation or [[scala.util.Right Right]] with the [[UUID UUID]] representation.
   */
  def from(s: String): Either[Throwable, UUID] = Try(JUUID.fromString(s).asScala).toEither

  /**
   * The nil UUID is special form of UUID that is specified to have all 128 bits set to zero.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.7]]
   */
  case object Nil extends UUID { override val juuid: JUUID = new JUUID(0, 0) }

  /**
   * Version 1 UUIDs are those generated using a timestamp and the MAC address of the
   * computer on which it was generated.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class V1 private[memeid] (override private[memeid] val juuid: JUUID) extends UUID {
    /**
     * Get the time_low component of the timestamp field
     * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.2]]
     * @return time_low component of the timestamp field
     */
    def timeLow: Long = readByte(mask(32, 0), juuid.timestamp())

    /**
     * Get the time_mid component of the timestamp field
     * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.2]]
     * @return time_mid component of the timestamp field
     */
    def timeMid: Long = readByte(mask(16, 32), juuid.timestamp())

    /**
     * Get the time_high component of the timestamp field
     * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.2]]
     * @return time_high component of the timestamp field
     */
    def timeHigh: Long = readByte(mask(12, 48), juuid.timestamp())

    /**
     * Get the clock_seq_low component of the clock sequence field
     * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.2]]
     * @return clock_seq_low component of the clock sequence field
     */
    def clockSeqLow: Long = readByte(mask(8, 0), juuid.clockSequence().toLong)

    /**
     * Get the clock_seq_high component of the clock sequence field
     * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.2]]
     * @return clock_seq_high component of the clock sequence field
     */
    def clockSeqHigh: Long = readByte(mask(6, 8), juuid.clockSequence().toLong)
  }

  /**
   * Companion object for [[V1]]
   */
  object V1 {

    /**
     * Construct a [[UUID.V1 V1]] (time-based) UUID.
     * @param N [[node.Node Node]] for the V1 UUID generation
     * @param T [[time.Time Time]] which assures the V1 UUID time is unique
     * @return [[UUID.V1 V1]]
     */
    def next(implicit N: Node, T: Time): UUID = {
      val timestamp = T.monotonic
      val low       = readByte(mask(32, 0), timestamp)
      val mid       = readByte(mask(16, 32), timestamp)
      val high      = readByte(mask(12, 48), timestamp)
      val msb       = Mask.version(high, 1) | (low << 32) | (mid << 16)

      val clkHigh = writeByte(mask(2, 6), readByte(mask(6, 8), N.id), 0x2)
      val clkLow  = readByte(mask(8, 0), N.clockSequence.toLong)
      val lsb     = writeByte(mask(8, 56), writeByte(mask(8, 48), N.id, clkLow), clkHigh)
      new UUID.V1(new JUUID(msb, lsb))
    }

  }

  /**
   * DCE Security version, with embedded POSIX UIDs.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class V2 private[memeid] (override private[memeid] val juuid: JUUID) extends UUID

  /**
   * Version 3 UUIDs are those generated by hashing a namespace identifier and name using
   * MD5 as the hashing algorithm.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class V3 private[memeid] (override private[memeid] val juuid: JUUID) extends UUID

  /**
   * Companion object for [[V3]]
   */
  object V3 {

    /**
     * Construct a namespace name-based v3 UUID. Uses MD5 as a hash algorithm
     * @param namespace [[UUID UUID]] used for the [[UUID.V3 V3]] generation
     * @param local name used for the [[UUID.V3 V3]] generation
     * @param D implicit [[digest.Digestible Digestible]] parameter
     * @tparam A Sets the type for the local and Digestible parameters
     * @return [[UUID.V3 V3]]
     */
    def apply[A](namespace: UUID, local: A)(implicit D: Digestible[A]): UUID =
      new UUID.V3(hashed(MD5, 3, namespace, local))

  }

  /**
   * Version 4 UUIDs are those generated using random numbers.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class V4 private[memeid] (override private[memeid] val juuid: JUUID) extends UUID

  /**
   * Companion object for [[V4]]
   */
  object V4 {

    /**
     * Construct a v4 (random) UUID from the given `msb` and `lsb`.
     * @param msb Most significant bit in [[scala.Long Long]] representation
     * @param lsb Least significant bit in [[scala.Long Long]] representation
     * @return  [[UUID.V4 V4]]
     */
    def apply(msb: Long, lsb: Long): UUID = {
      val v4msb = writeByte(mask(4, 12), msb, 0x4)
      val v4lsb = writeByte(mask(2, 62), lsb, 0x2)
      new UUID.V4(new JUUID(v4msb, v4lsb))
    }

    // Construct a v4 (random) UUID.
    def random: UUID = new UUID.V4(JUUID.randomUUID)

    // Construct a SQUUID (random, time-based) UUID.
    def squuid(implicit P: Posix): UUID = {
      val uuid = random

      val timedMsb = (P.value << 32) | (uuid.msb & Mask.UB32)

      new UUID.V4(new JUUID(timedMsb, uuid.lsb))
    }

  }

  /**
   * Version 5 UUIDs are those generated by hashing a namespace identifier and name using
   * SHA-1 as the hashing algorithm.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class V5 private[memeid] (override private[memeid] val juuid: JUUID) extends UUID

  /**
   * Companion object for [[V5]]
   */
  object V5 {

    /**
     * Construct a namespace name-based v5 UUID. Uses SHA as a hash algorithm
     * @param namespace [[UUID UUID]] used for the [[UUID.V5 V5]] generation
     * @param local name used for the [[UUID.V5 V5]] generation
     * @param D implicit [[digest.Digestible Digestible]] parameter
     * @tparam A Sets the type for the local and Digestible parameters
     * @return [[UUID.V5 V5]]
     */
    def apply[A](namespace: UUID, local: A)(implicit D: Digestible[A]): UUID =
      new UUID.V5(hashed(SHA1, 5, namespace, local))

  }

  /**
   * Not standard-version UUIDs. Includes the extracted version from the most significant bits.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class UnknownVersion private[memeid] (
      override private[memeid] val juuid: JUUID
  ) extends UUID

  private def hashed[A: Digestible](
      algo: Algorithm,
      version: Long,
      namespace: UUID,
      local: A
  ): JUUID = {
    val digest = algo.digest
    val ns     = Digestible[UUID].toByteArray(namespace)
    digest.update(ns)
    val name = Digestible[A].toByteArray(local)
    digest.update(name)
    val bytes  = digest.digest
    val rawMsb = fromBytes(bytes.take(8))
    val rawLsb = fromBytes(bytes.drop(8))
    val msb    = Mask.version(rawMsb, version)
    val lsb    = writeByte(mask(2, 52), rawLsb, 0x2)
    new JUUID(msb, lsb)
  }

  private object Mask {
    val VERSION: Long = mask(4, 12)
    val UB32: Long    = 0x00000000FFFFFFFFL

    def version(msb: Long, version: Long): Long =
      writeByte(VERSION, msb, version)
  }

  /**
   * Implicit [[memeid.digest.Digestible Digestible]] for converting an [[UUID]] to an Array of Bytes
   */
  implicit val DigestibleUUIDInstance: Digestible[UUID] =
    u => toBytes(u.msb) ++ toBytes(u.lsb)

}
