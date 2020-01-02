package memeid

import org.scalacheck.Gen
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.toString"))
class ConstructorsSpec extends Specification with ScalaCheck {

  "from" should {

    "return Some on valid UUID" in prop { uuid: UUID =>
      UUID.from(uuid.toString) should beRight(uuid)
    }

    "return None on invalid string" in prop { s: String =>
      UUID.from(s) must beLeft().like {
        case e: IllegalArgumentException => e.getMessage must contain("UUID string")
      }
    }.setGen(Gen.alphaNumStr)

  }

}
