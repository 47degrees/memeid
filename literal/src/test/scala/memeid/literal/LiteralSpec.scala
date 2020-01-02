package memeid.literal

import memeid.UUID
import org.specs2.mutable.Specification
import shapeless.test.illTyped

class LiteralSpec extends Specification {

  "uuid interpolator" should {

    "create UUID on valid string literal" >> {
      uuid"be5cb243-06a9-409e-899f-109d0ed8ea01" must beAnInstanceOf[UUID]
    }

    "fail on invalid string literal" >> {
      illTyped("""uuid"miau"""", "invalid UUID: miau") must beEqualTo(())
    }

  }

}
