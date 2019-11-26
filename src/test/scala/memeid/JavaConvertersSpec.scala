package memeid

import org.scalacheck.Prop.forAll
import memeid.JavaConverters._
import org.scalacheck.Properties

import cats.syntax.eq._

object JavaConvertersSpec extends Properties("UUID") {

  property("convert between java.util <-> memeid") = forAll { (msb: Long, lsb: Long) =>
    val uuid = UUID.from(msb, lsb)

    val juuid = uuid.asJava

    uuid === juuid.asScala
  }

}
