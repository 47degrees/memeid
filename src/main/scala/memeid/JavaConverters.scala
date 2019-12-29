package memeid

import java.util.{UUID => JUUID}

import cats.instances.uuid._
import cats.syntax.eq._

import memeid.UUID._

/**
 * A variety of decorators that enable converting between
 * Scala and Java UUIDs using extension methods, `asScala` and `asJava`.
 *
 * The extension methods return adapters for the corresponding API.
 *
 * The following conversions are supported via `asScala` and `asJava`:
 *
 * {{{ memeid.UUID <=> java.util.UUID }}}
 */
object JavaConverters {

  implicit final class JUUIDAsScala(private val juuid: JUUID) extends AnyVal {

    /** Converts this `java.util.UUID` into a `memeid.UUID` */
    def asScala: UUID = juuid.version() match {
      case 0 if juuid === Nil.juuid => Nil
      case 1                        => new V1(juuid)
      case 2                        => new V2(juuid)
      case 3                        => new V3(juuid)
      case 4                        => new V4(juuid)
      case 5                        => new V5(juuid)
      case _                        => new UnknownVersion(juuid)
    }

  }

  implicit final class UUIDAsJava(private val uuid: UUID) extends AnyVal {

    /** Converts this `memeid.UUID` into a `java.util.UUID` */
    def asJava: JUUID = uuid.juuid

  }

}
