package memeid

import cats.effect._
import cats.syntax.eq._

import memeid.digest._
import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class V3Spec extends Specification with ScalaCheck with IOMatchers {
  "Digestible" should {
    "give the same bytes for the same string" in {
      val str = "a-thing"
      Digestible[String].toByteArray(str) must be equalTo Digestible[String].toByteArray(str)
    }

    "give the same bytes for the same UUID" in {
      val uuid = UUID.v4
      Digestible[UUID].toByteArray(uuid) must be equalTo Digestible[UUID].toByteArray(uuid)
    }
  }

  "V3 constructor" should {
    "create same UUID for the same namespace/name" in {
      val test = for {
        ns <- UUID.v1[IO]
        name = "a-thing"
        first <- UUID.v3[IO, String](ns, name)
        last  <- UUID.v3[IO, String](ns, name)
      } yield first === last

      test must returnValue(true)
    }

    "create different UUIDs for distinct name" in {
      val test = for {
        ns <- UUID.v1[IO]
        name  = "a-thing"
        name2 = "another-thing"
        first <- UUID.v3[IO, String](ns, name)
        last  <- UUID.v3[IO, String](ns, name2)
      } yield first === last

      test must returnValue(false)
    }

    "create different UUIDs for distinct namespace" in {
      val test = for {
        ns  <- UUID.v1[IO]
        ns2 <- UUID.v1[IO]
        name  = "a-thing"
        local = "foo"
        first <- UUID.v3[IO, String](ns, name)
        last  <- UUID.v3[IO, String](ns2, name)
      } yield first === last

      test must returnValue(false)
    }
  }
}
