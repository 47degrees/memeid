package memeid

import java.lang.Long
import java.util.{UUID => JUUID}

import cats.Show
import cats.instances.uuid._
import cats.kernel._
import cats.syntax.contravariant._

sealed trait Version
case object Null                        extends Version
case object V1                          extends Version
case object V2                          extends Version
case object V3                          extends Version
case object V4                          extends Version
case object V5                          extends Version
final case class UnknownVersion(v: Int) extends Version

final case class UUID(private val juuid: JUUID) {

  @inline
  def msb: Long = juuid.getMostSignificantBits

  @inline
  def lsb: Long = juuid.getLeastSignificantBits

  @inline
  def variant: Int = juuid.variant

  @inline
  def version: Version = juuid.version match {
    case 0 => Null
    case 1 => V1
    case 2 => V2
    case 3 => V3
    case 4 => V4
    case 5 => V5
    case x => UnknownVersion(x)
  }

  def toJava: JUUID =
    juuid
}

object UUID {
  // The `null` UUID
  val empty: UUID = UUID(new JUUID(0, 0))

  /* Constructors */

  def apply(msb: Long, lsb: Long): UUID =
    UUID(new JUUID(msb, lsb))

  def fromJava(u: JUUID): UUID =
    UUID(u)

  /* Typeclass instances */

  implicit val UUIDHashOrderInstance: Order[UUID] with Hash[UUID] = new Order[UUID] with Hash[UUID] {

    override def hash(x: UUID): Int = Hash[JUUID].hash(x.juuid)

    override def compare(x: UUID, y: UUID): Int =
      Long.compareUnsigned(x.msb, y.msb) match {
        case 0 => Long.compareUnsigned(x.lsb, y.lsb)
        case x => x
      }

  }

  implicit val UUIDShowInstance: Show[UUID] = Show[JUUID].contramap(_.juuid)

}
