package memeid

import cats.effect._
import cats.syntax.eq._

import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class V3Spec extends Specification with ScalaCheck with IOMatchers {
  "V3 constructor" should {
    "create same UUID for the same namespace/name" in {
      val test = for {
        uuid <- UUID.v1[IO]
        local = "foo"
        first <- UUID.v3[IO, String](uuid, local)
        last  <- UUID.v3[IO, String](uuid, local)
      } yield first === last

      test must returnValue(true)
    }
  }
}
