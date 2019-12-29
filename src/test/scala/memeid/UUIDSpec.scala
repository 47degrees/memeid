package memeid

import java.util.{UUID => JUUID}

import cats._
import cats.instances.uuid._

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

  "UUID.as" should {

    "return Some[UUID.V1] only if version is 1" in prop { uuid: UUID.V1 =>
      (uuid.as[UUID.V1] must beSome(uuid)) and
        (uuid.as[UUID.V2] must beNone) and
        (uuid.as[UUID.V3] must beNone) and
        (uuid.as[UUID.V4] must beNone) and
        (uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V2] only if version is 2" in prop { uuid: UUID.V2 =>
      (uuid.as[UUID.V1] must beNone) and
        (uuid.as[UUID.V2] must beSome(uuid)) and
        (uuid.as[UUID.V3] must beNone) and
        (uuid.as[UUID.V4] must beNone) and
        (uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V3] only if version is 3" in prop { uuid: UUID.V3 =>
      (uuid.as[UUID.V1] must beNone) and
        (uuid.as[UUID.V2] must beNone) and
        (uuid.as[UUID.V3] must beSome(uuid)) and
        (uuid.as[UUID.V4] must beNone) and
        (uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V4] only if version is 4" in prop { uuid: UUID.V4 =>
      (uuid.as[UUID.V1] must beNone) and
        (uuid.as[UUID.V2] must beNone) and
        (uuid.as[UUID.V3] must beNone) and
        (uuid.as[UUID.V4] must beSome(uuid)) and
        (uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V5] only if version is 5" in prop { uuid: UUID.V5 =>
      (uuid.as[UUID.V1] must beNone) and
        (uuid.as[UUID.V2] must beNone) and
        (uuid.as[UUID.V3] must beNone) and
        (uuid.as[UUID.V4] must beNone) and
        (uuid.as[UUID.V5] must beSome(uuid))
    }

  }

  "UUID.is" should {

    "return true only if version is 1" in prop { uuid: UUID.V1 =>
      (uuid.is[UUID.V1] must beTrue) and
        (uuid.is[UUID.V2] must beFalse) and
        (uuid.is[UUID.V3] must beFalse) and
        (uuid.is[UUID.V4] must beFalse) and
        (uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 2" in prop { uuid: UUID.V2 =>
      (uuid.is[UUID.V1] must beFalse) and
        (uuid.is[UUID.V2] must beTrue) and
        (uuid.is[UUID.V3] must beFalse) and
        (uuid.is[UUID.V4] must beFalse) and
        (uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 3" in prop { uuid: UUID.V3 =>
      (uuid.is[UUID.V1] must beFalse) and
        (uuid.is[UUID.V2] must beFalse) and
        (uuid.is[UUID.V3] must beTrue) and
        (uuid.is[UUID.V4] must beFalse) and
        (uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 4" in prop { uuid: UUID.V4 =>
      (uuid.is[UUID.V1] must beFalse) and
        (uuid.is[UUID.V2] must beFalse) and
        (uuid.is[UUID.V3] must beFalse) and
        (uuid.is[UUID.V4] must beTrue) and
        (uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 5" in prop { uuid: UUID.V5 =>
      (uuid.is[UUID.V1] must beFalse) and
        (uuid.is[UUID.V2] must beFalse) and
        (uuid.is[UUID.V3] must beFalse) and
        (uuid.is[UUID.V4] must beFalse) and
        (uuid.is[UUID.V5] must beTrue)
    }

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

  "UUID.Nil" should {

    "have variant 0" in {
      UUID.Nil.variant must be equalTo 0
    }

    "have all 128 bits to 0" in {
      (UUID.Nil.msb must be equalTo 0L) and (UUID.Nil.lsb must be equalTo 0L)
    }

  }

}
