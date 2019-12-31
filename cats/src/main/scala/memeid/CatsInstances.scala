package memeid.cats

import java.lang.Long.compareUnsigned

import memeid._
import _root_.cats.Show
import _root_.cats.instances.uuid.catsStdShowForUUID
import _root_.cats.kernel.{Hash, LowerBounded, Order, PartialOrder, UpperBounded}
import _root_.cats.syntax.contravariant._

object implicits {

  implicit val UUIDHashOrderInstances: Order[UUID] with Hash[UUID] =
    new Order[UUID] with Hash[UUID] {

      override def hash(x: UUID): Int = x.juuid.hashCode /* scalafix:ok */ ()

      override def compare(x: UUID, y: UUID): Int =
        compareUnsigned(x.msb, y.msb) match {
          case 0 => compareUnsigned(x.lsb, y.lsb)
          case x => x
        }

    }

  implicit val UUIDShowInstance: Show[UUID] = catsStdShowForUUID.contramap(_.juuid)

  implicit val UUIDBoundsInstances: LowerBounded[UUID] with UpperBounded[UUID] =
    new LowerBounded[UUID] with UpperBounded[UUID] {

      override def minBound: UUID = UUID.Nil

      override def maxBound: UUID = UUID.from(-1L, -1L)

      override def partialOrder: PartialOrder[UUID] = UUIDHashOrderInstances
    }

}
