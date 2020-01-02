package memeid.digest

import org.specs2.mutable.Specification

class DigestableSpec extends Specification {

  "Digestible[String]" should {

    "give the same bytes for the same string" in {
      val str = "a-thing"

      Digestible[String].toByteArray(str) must be equalTo Digestible[String].toByteArray(str)
    }

  }

}
