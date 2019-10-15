package memeid

import cats.kernel._

import java.lang.Long
import java.util.{UUID => JUUID}

case class UUID(private val juuid: JUUID) {
  @inline 
  def msb: Long = juuid.getMostSignificantBits
  @inline
  def lsb: Long = juuid.getLeastSignificantBits

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

  implicit val orderForUUID: Order[UUID] with Hash[UUID] = new Order[UUID] with Hash[UUID]{
    override def eqv(x: UUID, y: UUID): Boolean = x.juuid.equals(y.juuid)
    def hash(x: UUID): Int = x.juuid.hashCode
    def compare(x: UUID, y: UUID): Int = {
      val mc = Long.compareUnsigned(x.msb, y.msb)
      if (mc != 0) {
        mc
      } else {
        Long.compareUnsigned(x.lsb, y.lsb)
      }
    }
  }
}
