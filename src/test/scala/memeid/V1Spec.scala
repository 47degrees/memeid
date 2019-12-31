package memeid

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("all"))
class V1Spec extends Specification with ScalaCheck {
  "V1 constructor" should {
    "create version 1 UUIDs" in {
      val ids = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).par

      val io = ids.map(_ => UUID.v1.version)

      io.toList.toSet must be equalTo (Set(1))
    }

    "create monotonically increasing UUIDs" in {
      val first = UUID.v1
      val last  = UUID.v1

      first.compareTo(last) must be lessThan (0)
    }

    "not generate the same UUID twice" in {
      val ids = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).par

      val io = ids.map(_ => UUID.v1)

      io.toSet.size must be equalTo (ids.size)
    }

    "not generate the same UUID twice with high concurrency" in {
      val ids = List.range(1, 999).toVector.par

      val io = ids.map(_ => UUID.v1)

      io.toSet.size must be equalTo (ids.size)
    }
  }
}
