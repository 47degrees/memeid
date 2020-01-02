package memeid

import java.util.{UUID => JUUID}

import memeid.JavaConverters._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class JavaConvertersSpec extends Specification with ScalaCheck {

  "We can convert between java.util <-> memeid" in prop { (msb: Long, lsb: Long) =>
    val uuid = UUID.from(msb, lsb)

    val juuid: JUUID = uuid.asJava

    uuid must be equalTo juuid.asScala
  }

  "V1 UUIDs are converted to UUID.V1" in {
    val uuid = JUUID.fromString("937026c6-1207-11ea-8d71-362b9e155667").asScala

    uuid must haveClass[UUID.V1]
  }

  "V3 UUIDs are converted to UUID.V3" in {
    val uuid = JUUID.fromString("a3bb189e-8bf9-3888-9912-ace4e6543002").asScala

    uuid must haveClass[UUID.V3]
  }

  "V4 UUIDs are converted to UUID.V4" in {
    val uuid = JUUID.randomUUID.asScala

    uuid must haveClass[UUID.V4]
  }

  "V5 UUIDs are converted to UUID.V5" in {
    val uuid = JUUID.fromString("a6edc906-2f9f-5fb2-a373-efac406f0ef2").asScala

    uuid must haveClass[UUID.V5]
  }

}
