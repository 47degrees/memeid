package memeid

import java.util.{UUID => JUUID}

import scala.concurrent.duration._

import cats.data._
import cats.effect._
import cats.syntax.apply._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.order._
import cats.syntax.parallel._

import memeid.JavaConverters._
import org.specs2.ScalaCheck
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification

class V1Spec extends Specification with ScalaCheck with IOMatchers {
  "V1 constructor" should {
    "create monotonically increasing UUIDs" in {
      val test = for {
        first <- UUID.v1[IO]
        last  <- UUID.v1[IO]
      } yield first < last

      test must returnValue(true)
    }

    "not generate the same UUID twice" in {
      @SuppressWarnings(Array("scalafix:Disable.get"))
      def ids = NonEmptyList.fromList(List.range(1, 10)).get
      val io  = ids.parTraverse(_ => UUID.v1[IO]).map(_.toList.toSet.size)
      io must returnValue(ids.size)
    }

    "not generate the same UUID twice with high concurrency" in {
      @SuppressWarnings(Array("scalafix:Disable.get"))
      def ids = NonEmptyList.fromList(List.range(1, 999)).get
      val io  = ids.parTraverse(_ => UUID.v1[IO]).map(_.toList.size)
      io must returnValue(ids.size)
    }

    def bench[F[_]: Sync: Clock, A](f: F[A]): F[(Long, A)] =
      for {
        start <- Clock[F].monotonic(NANOSECONDS)
        a     <- f
        end   <- Clock[F].monotonic(NANOSECONDS)
      } yield (end - start, a)

    "be faster than random generation" in {
      val random: IO[UUID] = IO.delay(JUUID.randomUUID.asScala)
      val v1: IO[UUID]     = UUID.v1[IO]
      val ((r, _), (v, _)) = (bench(random), bench(v1)).tupled.unsafeRunSync
      r must be greaterThan v
    }
  }
}
