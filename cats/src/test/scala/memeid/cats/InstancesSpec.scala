package memeid.cats

import java.util.{UUID => JUUID}

import cats.instances.uuid._
import cats.kernel.{Hash, Order}

import memeid.UUID
import memeid.cats.instances._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class InstancesSpec extends Specification with ScalaCheck {

  "Order[UUID]" should {

    "compare using unsigned comparison" in prop { lsb: Long =>
      // specifically chosen since they will not compare correctly unless using unsigned comparison
      val uuid1 = UUID.from(0x20000000.toLong, lsb)
      val uuid2 = UUID.from(0xE0000000.toLong, lsb)

      (Order[UUID].compare(uuid1, uuid2) must be equalTo -1) and
        (Order[JUUID].compare(uuid1.juuid, uuid2.juuid) must be equalTo 1)
    }

  }

  "Hash[UUID] returns hash code consistent with java.util.UUID" in prop { (msb: Long, lsb: Long) =>
    val uuid = UUID.from(msb, lsb)

    Hash[UUID].hash(uuid) must be equalTo Hash[JUUID].hash(uuid.juuid)
  }

}
