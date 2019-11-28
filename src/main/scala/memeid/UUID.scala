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

  @SuppressWarnings(Array("scalafix:Disable.equals", "scalafix:Disable.Any"))
  override def equals(obj: Any): Boolean = obj match {
    case x: UUID => Order[UUID].eqv(this, x)
    case _       => false
  }

  @SuppressWarnings(Array("scalafix:Disable.hashCode"))
  override def hashCode(): Int = Hash[UUID].hash(this)

  @SuppressWarnings(Array("scalafix:Disable.toString"))
  override def toString: String = Show[UUID].show(this)

}

object UUID {

  // The `null` UUID
  val empty: UUID = new UUID(new JUUID(0, 0))

  /* Constructors */

  def from(msb: Long, lsb: Long): UUID =
    new UUID(new JUUID(msb, lsb))

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
