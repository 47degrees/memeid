package memeid

import cats.effect._
import cats.syntax.eq._

import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class V5Spec extends Specification with ScalaCheck with IOMatchers {
  "V5 constructor" should {
    "create same UUID for the same namespace/name" in {
      val test = for {
        uuid <- UUID.v1[IO]
        local = "foo"
        first <- UUID.v5[IO, String](uuid, local)
        last  <- UUID.v5[IO, String](uuid, local)
      } yield first === last

      test must returnValue(true)
    }

    "create different UUIDs for distinct name" in {
      val test = for {
        ns <- UUID.v1[IO]
        name  = "a-thing"
        name2 = "another-thing"
        first <- UUID.v5[IO, String](ns, name)
        last  <- UUID.v5[IO, String](ns, name2)
      } yield first === last

      test must returnValue(false)
    }

    "create different UUIDs for distinct namespace" in {
      val test = for {
        ns  <- UUID.v1[IO]
        ns2 <- UUID.v1[IO]
        name  = "a-thing"
        local = "foo"
        first <- UUID.v5[IO, String](ns, name)
        last  <- UUID.v5[IO, String](ns2, name)
      } yield first === last

      test must returnValue(false)
    }
  }
}
