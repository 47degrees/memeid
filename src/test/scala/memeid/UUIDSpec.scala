package memeid

import java.util.{UUID => JUUID}

import cats._
import cats.data._
import cats.effect._
import cats.instances.all._
import cats.syntax.order._
import cats.syntax.functor._
import cats.syntax.apply._
import cats.syntax.flatMap._
import cats.syntax.parallel._

import memeid.JavaConverters._
import scala.concurrent.duration._

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class UUIDSpec extends Specification with ScalaCheck {

  "Order[UUID]" should {

    "compare using unsigned comparison" in prop { lsb: Long =>
      // specifically chosen since they will not compare correctly unless using unsigned comparison
      val uuid1 = UUID.from(0x20000000.toLong, lsb)
      val uuid2 = UUID.from(0xE0000000.toLong, lsb)

      (Order[UUID].compare(uuid1, uuid2) must be equalTo -1) and
        (Order[JUUID].compare(uuid1.juuid, uuid2.juuid) must be equalTo 1)
    }

  }

  "Hash[UUID] returns hash code consistent with java.util.UUID" in prop { (msb: Long, lsb: Long) =>
    val uuid = UUID.from(msb, lsb)

    Hash[UUID].hash(uuid) must be equalTo Hash[JUUID].hash(uuid.juuid)
  }

  "UUID.as" should {

    "return Some[UUID.V1] only if version is 1" in prop { uuid: UUID.V1 =>
      (uuid.as[UUID.V1] must beSome(uuid)) and
        (uuid.as[UUID.V2] must beNone) and
        (uuid.as[UUID.V3] must beNone) and
        (uuid.as[UUID.V4] must beNone) and
        (uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V2] only if version is 2" in prop { uuid: UUID.V2 =>
      (uuid.as[UUID.V1] must beNone) and
        (uuid.as[UUID.V2] must beSome(uuid)) and
        (uuid.as[UUID.V3] must beNone) and
        (uuid.as[UUID.V4] must beNone) and
        (uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V3] only if version is 3" in prop { uuid: UUID.V3 =>
      (uuid.as[UUID.V1] must beNone) and
        (uuid.as[UUID.V2] must beNone) and
        (uuid.as[UUID.V3] must beSome(uuid)) and
        (uuid.as[UUID.V4] must beNone) and
        (uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V4] only if version is 4" in prop { uuid: UUID.V4 =>
      (uuid.as[UUID.V1] must beNone) and
        (uuid.as[UUID.V2] must beNone) and
        (uuid.as[UUID.V3] must beNone) and
        (uuid.as[UUID.V4] must beSome(uuid)) and
        (uuid.as[UUID.V5] must beNone)
    }

    "return Some[UUID.V5] only if version is 5" in prop { uuid: UUID.V5 =>
      (uuid.as[UUID.V1] must beNone) and
        (uuid.as[UUID.V2] must beNone) and
        (uuid.as[UUID.V3] must beNone) and
        (uuid.as[UUID.V4] must beNone) and
        (uuid.as[UUID.V5] must beSome(uuid))
    }

  }

  "UUID.is" should {

    "return true only if version is 1" in prop { uuid: UUID.V1 =>
      (uuid.is[UUID.V1] must beTrue) and
        (uuid.is[UUID.V2] must beFalse) and
        (uuid.is[UUID.V3] must beFalse) and
        (uuid.is[UUID.V4] must beFalse) and
        (uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 2" in prop { uuid: UUID.V2 =>
      (uuid.is[UUID.V1] must beFalse) and
        (uuid.is[UUID.V2] must beTrue) and
        (uuid.is[UUID.V3] must beFalse) and
        (uuid.is[UUID.V4] must beFalse) and
        (uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 3" in prop { uuid: UUID.V3 =>
      (uuid.is[UUID.V1] must beFalse) and
        (uuid.is[UUID.V2] must beFalse) and
        (uuid.is[UUID.V3] must beTrue) and
        (uuid.is[UUID.V4] must beFalse) and
        (uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 4" in prop { uuid: UUID.V4 =>
      (uuid.is[UUID.V1] must beFalse) and
        (uuid.is[UUID.V2] must beFalse) and
        (uuid.is[UUID.V3] must beFalse) and
        (uuid.is[UUID.V4] must beTrue) and
        (uuid.is[UUID.V5] must beFalse)
    }

    "return true only if version is 5" in prop { uuid: UUID.V5 =>
      (uuid.is[UUID.V1] must beFalse) and
        (uuid.is[UUID.V2] must beFalse) and
        (uuid.is[UUID.V3] must beFalse) and
        (uuid.is[UUID.V4] must beFalse) and
        (uuid.is[UUID.V5] must beTrue)
    }

  }

  "UUID.variant" should {

    "detect a valid variant" in prop { msb: Long =>
      val uuid = UUID.from(msb, 0x9000000000000000L)

      uuid.variant must be equalTo 2
    }

    "detect an invalid UUID variant" in prop { msb: Long =>
      val uuid = UUID.from(msb, 0xC000000000000000L)

      uuid.variant must not be equalTo(2)
    }

  }

  "UUID.Nil" should {

    "have variant 0" in {
      UUID.Nil.variant must be equalTo 0
    }

    "have all 128 bits to 0" in {
      (UUID.Nil.msb must be equalTo 0L) and (UUID.Nil.lsb must be equalTo 0L)
    }

  }

}

class V1Spec extends Specification with ScalaCheck {
  implicit val cs: ContextShift[IO] = IO.contextShift(scala.concurrent.ExecutionContext.global)
  implicit val clk: Clock[IO]       = Clock.create[IO]

  "V1 constructor" should {
    "create monotonically increasing UUIDs" in {
      val test = (for {
        first <- UUID.v1[IO]
        last  <- UUID.v1[IO]
      } yield first < last).unsafeRunSync

      test must be equalTo (true)
    }

    "not generate the same UUID twice" in {
      @SuppressWarnings(Array("scalafix:Disable.get"))
      def ids = NonEmptyList.fromList(List.range(1, 10)).get
      val io  = ids.parTraverse(_ => UUID.v1[IO]).unsafeRunSync.toList
      io.toSet.size must be equalTo ids.size
    }

    "not generate the same UUID twice with high concurrency" in {
      @SuppressWarnings(Array("scalafix:Disable.get"))
      def ids = NonEmptyList.fromList(List.range(1, 999)).get
      val io  = ids.parTraverse(_ => UUID.v1[IO]).unsafeRunSync.toList
      io.toSet.size must be equalTo ids.size
    }

    // todo: more thorough testing of this
    def bench[F[_] : Sync : Clock, A](f: F[A]): F[(Long, A)] = for {
      start <- Clock[F].monotonic(NANOSECONDS)
      a <- f
      end <- Clock[F].monotonic(NANOSECONDS)
    } yield (end - start, a)

    "be faster than random generation" in {
      val random: IO[UUID] = IO.delay(JUUID.randomUUID.asScala)
      val v1: IO[UUID] = UUID.v1[IO]
      val ((r, _), (v, _)) = (bench(random), bench(v1)).tupled.unsafeRunSync
      r must be greaterThan v
    }
  }
}
