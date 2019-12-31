package memeid

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("all"))
class V4Spec extends Specification with ScalaCheck {
  "V4 constructor" should {
    "create version 4 UUIDs" in {
      val ids = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).par

      val io = ids.map(_ => UUID.v4.version)

      io.toList.toSet must be equalTo (Set(4))
    }

    "not generate the same UUID twice" in {
      val ids = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).par

      val io = ids.map(_ => UUID.v4)

      io.toSet.size must be equalTo (ids.size)
    }

    "be unable to create non-v4 values regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.v4(0, 0)
      nonNull must not be equalTo(UUID.Nil)
    }

    "generate version 4 UUIDs regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.v4(0, 0)
      nonNull.version must be equalTo (4)
    }
  }
}
