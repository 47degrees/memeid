package memeid

import java.lang.Long.compareUnsigned
import java.util.{UUID => JUUID}

import cats.Show
import cats.instances.uuid._
import cats.kernel.{Hash, Order}

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait CatsInstances {

  implicit val UUIDHashOrderShowInstances: Order[UUID] with Hash[UUID] with Show[UUID] =
    new Order[UUID] with Hash[UUID] with Show[UUID] {

      override def hash(x: UUID): Int = Hash[JUUID].hash(x.juuid)

      override def compare(x: UUID, y: UUID): Int =
        compareUnsigned(x.msb, y.msb) match {
          case 0 => compareUnsigned(x.lsb, y.lsb)
          case x => x
        }

      override def show(t: UUID): String = Show[JUUID].show(t.juuid)
    }

}
