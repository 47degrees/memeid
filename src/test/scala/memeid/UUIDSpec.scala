package memeid

import cats._
import cats.instances.all._
import cats.syntax.eq._

import org.scalacheck.Prop.forAll
import org.scalacheck._

object UUIDSpec extends Properties("UUID") {
  property("round-trips as java.util.UUID") = forAll { (msb: Long, lsb: Long) =>
    val uuid   = UUID.from(msb, lsb)
    val asJava = uuid.toJava
    val asUuid = UUID.fromJava(asJava)
    uuid === asUuid
  }

  property("compares properly using unsigned comparison") = forAll { (lsb: Long) =>
    // specifically chosen since they will not compare correctly unless using unsigned comparison
    val msb         = 0x20000000
    val lmsb        = 0xE0000000
    val aUuid       = UUID.from(msb.toLong, lsb)
    val anotherUuid = UUID.from(lmsb.toLong, lsb)

    Order[UUID].compare(aUuid, anotherUuid) === -1 &&
    Order[java.util.UUID].compare(aUuid.toJava, anotherUuid.toJava) === 1
  }

  property("hash code is consistent with java.util.UUID") = forAll { (msb: Long, lsb: Long) =>
    val uuid   = UUID.from(msb, lsb)
    val asJava = uuid.toJava
    Hash[UUID].hash(uuid) === Hash[java.util.UUID].hash(asJava)
  }

  property("detect a valid UUID variant") = forAll { (msb: Long) =>
    val lsb: Long = 0x9000000000000000L
    val uuid      = UUID.from(msb, lsb)
    uuid.variant === 2
  }

  property("detect an invalid UUID variant") = forAll { (msb: Long) =>
    val lsb: Long = 0xC000000000000000L
    val uuid      = UUID.from(msb, lsb)
    !(uuid.variant === 2)
  }

  property("Null UUID version and variant are Null") = forAll { (_: Long) =>
    UUID.empty.version == Version.Null &&
    UUID.empty.variant == 0
  }

  property("Possible versions are converted to ADT") = forAll { (msb: Long, lsb: Long) =>
    val uuid = UUID.from(msb, lsb)
    uuid.version == Version.Null |
      uuid.version == Version.V1 |
      uuid.version == Version.V2 |
      uuid.version == Version.V3 |
      uuid.version == Version.V4 |
      uuid.version == Version.V5 |
      uuid.version.isInstanceOf[Version.UnknownVersion]
  }
}
