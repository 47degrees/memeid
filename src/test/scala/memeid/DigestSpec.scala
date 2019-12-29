package memeid

import memeid.digest._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class DigestSpec extends Specification with ScalaCheck {
  "UUID as byte array" should {
    "round-trip" in prop { (msb: Long, lsb: Long) =>
      val uuid  = UUID.from(msb, lsb)
      val bytes = Digestible[UUID].toByteArray(uuid)
      UUID.fromByteArray(bytes) must be equalTo uuid
    }
  }
}
