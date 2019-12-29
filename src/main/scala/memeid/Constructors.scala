package memeid

import java.util.{UUID => JUUID}

import cats.effect._
import cats.syntax.apply._
import cats.syntax.flatMap._

import memeid.JavaConverters._
import memeid.bits._
import memeid.node._
import memeid.time._

trait Constructors {

  /**
   * Creates a valid [[UUID]] from two [[Long]] values representing
   * the most/least significant bits.
   */
  def from(msb: Long, lsb: Long): UUID = new JUUID(msb, lsb).asScala

  // TODO: memoize
  private def v1Lsb(clockSequence: Short, nodeId: Long): Long = {
    val clkHigh = writeByte(mask(2, 6), readByte(mask(6, 8), nodeId), 0x2)
    val clkLow  = readByte(mask(8, 0), clockSequence.toLong)
    writeByte(mask(8, 56), writeByte(mask(8, 48), nodeId, clkLow), clkHigh)
  }

  def v1[F[_]: Sync: Time](implicit N: Node[F]): F[UUID] =
    Time[F].monotonic.flatMap { ts =>
      val low  = readByte(mask(32, 0), ts)
      val mid  = readByte(mask(16, 32), ts)
      val high = writeByte(mask(4, 12), readByte(mask(12, 48), ts), 0x1)
      val msb  = high | (low << 32) | (mid << 16)
      (N.clockSequence, N.nodeId).mapN({
        case (clkSeq, nodeId) => new UUID.V1(new JUUID(msb, v1Lsb(clkSeq, nodeId)))
      })
    }

}
