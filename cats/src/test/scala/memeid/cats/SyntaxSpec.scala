package memeid.cats

import cats.effect.IO

import memeid.UUID
import memeid.cats.syntax._
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class SyntaxSpec extends Specification with IOMatchers {

  "v1 constructor" should {

    "create version 1 UUIDs" >> {
      UUID.v1[IO] must returnValue(anInstanceOf[UUID.V1])
    }

  }

  "v3 constructor" should {

    "create version 3 UUIDs" >> {
      UUID.v3[IO, String](UUID.V4.random, "hi") must returnValue(anInstanceOf[UUID.V3])
    }

  }

  "random constructor" should {

    "create version 4 UUIDs" >> {
      UUID.random[IO] must returnValue(anInstanceOf[UUID.V4])
    }

  }

  "squuid constructor" should {

    "create version 4 UUIDs" >> {
      UUID.squuid[IO] must returnValue(anInstanceOf[UUID.V4])
    }

  }

  "v5 constructor" should {

    "create version 5 UUIDs" >> {
      UUID.v5[IO, String](UUID.V4.random, "hi") must returnValue(anInstanceOf[UUID.V5])
    }

  }

}
