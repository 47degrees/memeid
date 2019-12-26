package memeid

import cats.effect.IO

import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class SQUUIDSpec extends Specification with ScalaCheck with IOMatchers {
  "SQUUID constructor" should {
    "generate version 4 UUIDs" in {
      val uuid = UUID.squuid[IO]
      uuid.map(_.juuid.version) must returnValue(4)
    }
  }
}
