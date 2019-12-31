package memeid

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("all"))
class SQUUIDSpec extends Specification with ScalaCheck {
  "SQUUID constructor" should {
    "create version 4 UUIDs" in {
      def ids = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).par

      val io = ids.map(_ => UUID.squuid.version)

      io.toList.toSet must be equalTo (Set(4))
    }

    "not generate the same UUID twice" in {
      def ids = Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).par

      val io = ids.map(_ => UUID.squuid)

      io.toSet.size must be equalTo (ids.size)
    }
  }
}
