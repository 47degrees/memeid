package memeid

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.map", "scalafix:Disable.to"))
class V4Spec extends Specification with ScalaCheck {

  "V4 constructor" should {

    "create version 4 UUIDs" in {
      val uuids = (1 to 10).par.map(_ => UUID.V4.random.version)

      uuids.to[Set] must contain(exactly(4))
    }

    "not generate the same UUID twice" in {
      val uuids = (1 to 10).par.map(_ => UUID.V4.random)

      uuids.to[Set].size must be equalTo 10
    }

    "be unable to create non-v4 values regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.V4(0, 0)
      nonNull must not be equalTo(UUID.Nil)
    }

    "generate version 4 UUIDs regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.V4(0, 0)
      nonNull.version must be equalTo 4
    }

  }

}
