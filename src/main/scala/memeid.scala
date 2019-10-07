package memeid

import cats.kernel._

import java.lang.Long
import java.util.{UUID => JUUID}

case class UUID(msb: Long, lsb: Long) 

object UUID {
  val empty: UUID = UUID(0, 0)

  def toJavaUUID(u: UUID): JUUID =
    new JUUID(u.msb, u.lsb)

  def fromJavaUUID(u: JUUID): UUID =
    UUID(u.getMostSignificantBits, u.getLeastSignificantBits)

  implicit val orderForUUID: Order[UUID] = new Order[UUID]{
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


