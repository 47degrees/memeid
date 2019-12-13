package memeid

import cats.implicits._

import org.scalacheck.Gen
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class ConstructorsSpec extends Specification with ScalaCheck {

  "from" should {

    "return Some on valid UUID" in prop { uuid: UUID =>
      UUID.from(uuid.show) must beRight(uuid)
    }

    "return None on invalid string" in prop { s: String =>
      UUID.from(s) must beLeft().like {
        case e: IllegalArgumentException => e.getMessage must be equalTo s"Invalid UUID string: $s"
      }
    }.setGen(Gen.alphaNumStr)

  }

}
