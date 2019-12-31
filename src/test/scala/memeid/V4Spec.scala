package memeid

import cats.data._
import cats.effect._
import cats.syntax.parallel._

import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class V4Spec extends Specification with ScalaCheck with IOMatchers {
  "V4 constructor" should {
    "create version 4 UUIDs" in {
      @SuppressWarnings(Array("scalafix:Disable.get"))
      def ids = NonEmptyList.fromList(List.range(1, 10)).get

      val io = for {
        uuids <- ids.parTraverse(_ => UUID.v4[IO])
        versions = uuids.map(_.version)
      } yield versions.toList.toSet

      io must returnValue[Set[Int]](Set(4))
    }

    "not generate the same UUID twice" in {
      @SuppressWarnings(Array("scalafix:Disable.get"))
      def ids = NonEmptyList.fromList(List.range(1, 10)).get
      val io  = ids.parTraverse(_ => UUID.v4[IO]).map(_.toList.toSet.size)
      io must returnValue(ids.size)
    }

    "be unable to create non-v4 values regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.v4[IO](0, 0).unsafeRunSync
      nonNull must not be equalTo(UUID.Nil)
    }
  }
}
