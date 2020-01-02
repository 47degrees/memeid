package memeid

import java.nio.ByteBuffer
import java.util.{UUID => JUUID}

import memeid.JavaConverters._
import memeid.digest._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class DigestSpec extends Specification with ScalaCheck {

  "UUID as byte array" should {
    "round-trip" in prop { (msb: Long, lsb: Long) =>
      val uuid  = UUID.from(msb, lsb)
      val bytes = Digestible[UUID].toByteArray(uuid)
      fromByteArray(bytes) must be equalTo uuid
    }
  }

  def fromByteArray(bytes: Array[Byte]): UUID = {
    val bb = ByteBuffer.wrap(bytes)
    new JUUID(bb.getLong, bb.getLong).asScala
  }

}
