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

}
