package memeid

import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.map", "scalafix:Disable.to"))
class SQUUIDSpec extends Specification with ScalaCheck with IOMatchers {

  "SQUUID constructor" should {

    "create version 4 UUIDs" in {
      val uuids = (1 to 10).par.map(_ => UUID.V4.squuid.version)

      uuids.to[Set] must contain(exactly(4))
    }

    "not generate the same UUID twice" in {
      val uuids = (1 to 10).par.map(_ => UUID.V4.squuid)

      uuids.to[Set].size must be equalTo 10
    }

  }

}
