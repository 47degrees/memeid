package memeid

import cats.data._
import cats.effect.IO
import cats.syntax.parallel._

import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class SQUUIDSpec extends Specification with ScalaCheck with IOMatchers {
  "SQUUID constructor" should {
    "create version 4 UUIDs" in {
      @SuppressWarnings(Array("scalafix:Disable.get"))
      def ids = NonEmptyList.fromList(List.range(1, 10)).get

      val io = for {
        uuids <- ids.parTraverse(_ => UUID.squuid[IO])
        versions = uuids.map(_.version)
      } yield versions.toList.toSet

      io must returnValue[Set[Int]](Set(4))
    }

    "not generate the same UUID twice" in {
      @SuppressWarnings(Array("scalafix:Disable.get"))
      def ids = NonEmptyList.fromList(List.range(1, 10)).get
      val io  = ids.parTraverse(_ => UUID.squuid[IO]).map(_.toList.toSet.size)
      io must returnValue(ids.size)
    }
  }
}
