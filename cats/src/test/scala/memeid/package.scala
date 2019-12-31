package memeid

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

object arbitraries {

  implicit val UUID2UUIDArbitraryInstance: Arbitrary[UUID => UUID] = Arbitrary(
    arbitrary[UUID].map(uuid => { _: UUID => uuid })
  )

  implicit val UUIDArbitraryInstance: Arbitrary[UUID] = Arbitrary {
    for {
      msb <- arbitrary[Long]
      lsb <- arbitrary[Long]
    } yield UUID.from(msb, lsb)
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV1ArbitraryInstance: Arbitrary[UUID.V1] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V1]).map(uuid => new UUID.V1(uuid.juuid))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV2ArbitraryInstance: Arbitrary[UUID.V2] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V2]).map(uuid => new UUID.V2(uuid.juuid))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV3ArbitraryInstance: Arbitrary[UUID.V3] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V3]).map(uuid => new UUID.V3(uuid.juuid))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV4ArbitraryInstance: Arbitrary[UUID.V4] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V4]).map(uuid => new UUID.V4(uuid.juuid))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV5ArbitraryInstance: Arbitrary[UUID.V5] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V5]).map(uuid => new UUID.V5(uuid.juuid))
  }

}
