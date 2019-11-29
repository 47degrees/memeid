import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

package object memeid {

  implicit val UUIDGenInstance: Arbitrary[UUID] = Arbitrary {
    for {
      msb <- arbitrary[Long]
      lsb <- arbitrary[Long]
    } yield UUID.from(msb, lsb)
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV1GenInstance: Arbitrary[UUID.V1] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V1]).map(uuid => new UUID.V1(uuid.juuid))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV2GenInstance: Arbitrary[UUID.V2] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V2]).map(uuid => new UUID.V2(uuid.juuid))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV3GenInstance: Arbitrary[UUID.V3] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V3]).map(uuid => new UUID.V3(uuid.juuid))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV4GenInstance: Arbitrary[UUID.V4] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V4]).map(uuid => new UUID.V4(uuid.juuid))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV5GenInstance: Arbitrary[UUID.V5] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V5]).map(uuid => new UUID.V5(uuid.juuid))
  }

}
