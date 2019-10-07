package memeid

import cats._
import cats.syntax.eq._
import cats.instances.all._
import org.scalacheck._
import org.scalacheck.Prop.forAll

object UUIDSpec extends Properties("UUID") {
  property("round-trips as java.util.UUID") = forAll { (msb: Long, lsb: Long) => 
    val uuid = UUID(msb, lsb)
    val asJava = UUID.toJavaUUID(uuid)
    val asUuid = UUID.fromJavaUUID(asJava)
    uuid === asUuid
  }

  property("compares properly using unsigned comparison") = forAll { (lsb: Long) =>
    // specifically chosen since they will not compare correctly unless using unsigned comparison
    val msb = 0x20000000
    val lmsb = 0xE0000000
    val aUuid = UUID(msb, lsb)
    val anotherUuid = UUID(lmsb, lsb)

    Order[UUID].compare(
      aUuid,
      anotherUuid
    ) === -1 &&
    Order[java.util.UUID].compare(
      UUID.toJavaUUID(aUuid),
      UUID.toJavaUUID(anotherUuid)
    ) === 1
  }  

}
