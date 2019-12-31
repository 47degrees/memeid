package memeid.cats

import _root_.cats.effect._

import memeid.UUID
import memeid.node._
import memeid.time._

object FUUID {

  // Construct a v1 (time-based) UUID.
  def v1[F[_] : Sync](implicit N: Node, T: Time): F[UUID] = Sync[F].delay(UUID.v1(N, T))

  // Construct a v4 (random) UUID.
  def v4[F[_] : Sync]: F[UUID] = Sync[F].delay(UUID.v4)

  def random[F[_] : Sync]: F[UUID] = v4

  // Construct a v4 (random) UUID from the given `msb` and `lsb`.
  def v4[F[_] : Sync](msb: Long, lsb: Long): F[UUID] = Sync[F].delay(UUID.v4(msb, lsb))

  def random[F[_] : Sync](msb: Long, lsb: Long): F[UUID] = v4(msb, lsb)

  // Construct a SQUUID (random, time-based) UUID.
  def squuid[F[_] : Sync](implicit T: Time): F[UUID] = Sync[F].delay(UUID.squuid)
}
