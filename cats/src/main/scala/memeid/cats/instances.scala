package memeid.cats

import java.lang.Long.compareUnsigned

import cats.Show
import cats.instances.uuid.catsStdShowForUUID
import cats.kernel._
import cats.syntax.contravariant._

import memeid.UUID

trait instances {

  implicit def UUIDHashOrderInstances: Order[UUID] with Hash[UUID] =
    new Order[UUID] with Hash[UUID] {

      override def hash(x: UUID): Int = x.juuid.hashCode /* scalafix:ok */ ()

      override def compare(x: UUID, y: UUID): Int =
        compareUnsigned(x.msb, y.msb) match {
          case 0 => compareUnsigned(x.lsb, y.lsb)
          case x => x
        }

    }

  implicit def UUIDShowInstance: Show[UUID] = catsStdShowForUUID.contramap(_.juuid)

  implicit def UUIDBoundsInstances: LowerBounded[UUID] with UpperBounded[UUID] =
    new LowerBounded[UUID] with UpperBounded[UUID] {

      override def minBound: UUID = UUID.Nil

      override def maxBound: UUID = UUID.from(-1L, -1L)

      override def partialOrder: PartialOrder[UUID] = UUIDHashOrderInstances
    }

}

object instances extends instances
