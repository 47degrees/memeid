package memeid

import java.util.{UUID => JUUID}

import cats.effect._
import cats.syntax.apply._
import cats.syntax.flatMap._

import memeid.JavaConverters._
import memeid.bits._
import memeid.node._
import memeid.time._

protected[memeid] object Mask {
  val VERSION: Long = mask(4, 12)

  def version(msb: Long, version: Long): Long =
    writeByte(VERSION, msb, version)
}

trait Constructors {

  /**
   * Creates a valid [[UUID]] from two [[Long]] values representing
   * the most/least significant bits.
   */
  def from(msb: Long, lsb: Long): UUID = new JUUID(msb, lsb).asScala

  def v1[F[_]: Sync](implicit N: Node[F], T: Time[F]): F[UUID] =
    T.monotonic.flatMap { ts =>
      val low  = readByte(mask(32, 0), ts)
      val mid  = readByte(mask(16, 32), ts)
      val high = readByte(mask(12, 48), ts)
      val msb  = Mask.version(high, 1) | (low << 32) | (mid << 16)
      (N.clockSequence, N.nodeId).mapN({
        case (clkSeq, nodeId) => {
          val clkHigh = writeByte(mask(2, 6), readByte(mask(6, 8), nodeId), 0x2)
          val clkLow  = readByte(mask(8, 0), clkSeq.toLong)
          val lsb     = writeByte(mask(8, 56), writeByte(mask(8, 48), nodeId, clkLow), clkHigh)
          new UUID.V1(new JUUID(msb, lsb))
        }
      })
    }

}
