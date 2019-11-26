package memeid

import java.lang.Long
import java.util.{UUID => JUUID}

import cats.Show
import cats.instances.uuid._
import cats.kernel._
import cats.syntax.contravariant._

final class UUID private[memeid] (private[memeid] val juuid: JUUID) {

  @inline
  def msb: Long = juuid.getMostSignificantBits

  @inline
  def lsb: Long = juuid.getLeastSignificantBits

  @inline
  def variant: Int = juuid.variant

  @inline
  def version: Version = juuid.version match {
    case 0 => Version.Null
    case 1 => Version.V1
    case 2 => Version.V2
    case 3 => Version.V3
    case 4 => Version.V4
    case 5 => Version.V5
    case x => Version.UnknownVersion(x)
  }

  def toJava: JUUID =
    juuid
}

object UUID {

  // The `null` UUID
  val empty: UUID = new UUID(new JUUID(0, 0))

  /* Constructors */

  def apply(msb: Long, lsb: Long): UUID =
    new UUID(new JUUID(msb, lsb))

  def fromJava(u: JUUID): UUID =
    new UUID(u)

  /* Typeclass instances */

  implicit val UUIDHashOrderInstance: Order[UUID] with Hash[UUID] =
    new Order[UUID] with Hash[UUID] {

      override def hash(x: UUID): Int = Hash[JUUID].hash(x.juuid)

      override def compare(x: UUID, y: UUID): Int =
        Long.compareUnsigned(x.msb, y.msb) match {
          case 0 => Long.compareUnsigned(x.lsb, y.lsb)
          case x => x
        }

    }

  implicit val UUIDShowInstance: Show[UUID] = Show[JUUID].contramap(_.juuid)

}
