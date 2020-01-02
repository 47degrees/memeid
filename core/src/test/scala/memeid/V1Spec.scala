package memeid

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.map", "scalafix:Disable.to"))
class V1Spec extends Specification with ScalaCheck {

  "V1 constructor" should {

    "create version 1 UUIDs" in {
      val ids = (1 to 10).par.map(_ => UUID.v1.version)

      ids.to[Set] must contain(exactly(1))
    }

    "create monotonically increasing UUIDs" in {
      val uuid1 = UUID.v1
      val uuid2 = UUID.v1

      uuid1 must be lessThan uuid2
    }

    "not generate the same UUID twice" in {
      val ids = (1 to 10).par.map(_ => UUID.v1)

      ids.size must be equalTo 10
    }

    "not generate the same UUID twice with high concurrency" in {
      val ids = (1 to 999).par.map(_ => UUID.v1)

      ids.size must be equalTo 999
    }

  }

}
