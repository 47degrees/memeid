package memeid

import java.util.{UUID => JUUID}

import cats._
import cats.instances.all._

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class UUIDSpec extends Specification with ScalaCheck {

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

  "UUID.variant" should {

    "detect a valid variant" in prop { msb: Long =>
      val uuid = UUID.from(msb, 0x9000000000000000L)

      uuid.variant must be equalTo 2
    }

    "detect an invalid UUID variant" in prop { msb: Long =>
      val uuid = UUID.from(msb, 0xC000000000000000L)

      uuid.variant must not be equalTo(2)
    }

  }

  "UUID.empty" should {

    "have variant 0" in {
      UUID.empty.variant must be equalTo 0
    }

    "have all 128 bits to 0" in {
      (UUID.empty.msb must be equalTo 0L) and (UUID.empty.lsb must be equalTo 0L)
    }

  }

}
