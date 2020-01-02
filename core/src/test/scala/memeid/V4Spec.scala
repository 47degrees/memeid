package memeid

import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.map", "scalafix:Disable.to"))
class V4Spec extends Specification with ScalaCheck with IOMatchers {

  "V4 constructor" should {

    "create version 4 UUIDs" in {
      val uuids = (1 to 10).par.map(_ => UUID.v4.version)

      uuids.to[Set] must contain(exactly(4))
    }

    "not generate the same UUID twice" in {
      val uuids = (1 to 10).par.map(_ => UUID.v4)

      uuids.to[Set].size must be equalTo 10
    }

    "be unable to create non-v4 values regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.v4(0, 0)
      nonNull must not be equalTo(UUID.Nil)
    }

    "generate version 4 UUIDs regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.v4(0, 0)
      nonNull.version must be equalTo 4
    }

  }

}
