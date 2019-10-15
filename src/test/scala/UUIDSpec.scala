package memeid

import cats._
import cats.syntax.eq._
import cats.instances.all._

import org.scalacheck._
import org.scalacheck.Prop.forAll

object UUIDSpec extends Properties("UUID") {
  property("round-trips as java.util.UUID") = forAll { (msb: Long, lsb: Long) =>
    val uuid = UUID(msb, lsb)
    val asJava = uuid.toJava
    val asUuid = UUID.fromJava(asJava)
    uuid === asUuid
  }

  property("compares properly using unsigned comparison") = forAll {
    (lsb: Long) =>
      // specifically chosen since they will not compare correctly unless using unsigned comparison
      val msb = 0x20000000
      val lmsb = 0xE0000000
      val aUuid = UUID(msb, lsb)
      val anotherUuid = UUID(lmsb, lsb)

      Order[UUID].compare(aUuid, anotherUuid) === -1 &&
      Order[java.util.UUID].compare(aUuid.toJava, anotherUuid.toJava) === 1
  }

  property("hash code is consistent with java.util.UUID") = forAll {
    (msb: Long, lsb: Long) =>
      val uuid = UUID(msb, lsb)
      val asJava = uuid.toJava
      Hash[UUID].hash(uuid) === Hash[java.util.UUID].hash(asJava)
  }

  property("detect a valid UUID variant") = forAll { (msb: Long) =>
    val lsb: Long = 0x9000000000000000L
    val uuid = UUID(msb, lsb)
    uuid.variant === 2
  }

  property("detect an invalid UUID variant") = forAll { (msb: Long) =>
    val lsb: Long = 0xC000000000000000L
    val uuid = UUID(msb, lsb)
    !(uuid.variant === 2)
  }

}
