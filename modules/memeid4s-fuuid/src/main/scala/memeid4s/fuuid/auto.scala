package memeid4s.fuuid

import io.chrisdavenport.fuuid.FUUID
import memeid4s.UUID

@SuppressWarnings(Array("scalafix:DisableSyntax.implicitConversion"))
object auto {

  /**
   * Provides an automatic conversion between a `FUUID` and a memeid's `UUID`.
   */
  implicit def FUUID2UUIDOps(fuuid: FUUID): UUID = UUID.fromUUID(FUUID.Unsafe.toUUID(fuuid))

  /**
   * Provides an automatic conversion between a memeid's `UUID` and a `FUUID`.
   */
  implicit def UUID2FUUIDOps(uuid: UUID): FUUID = FUUID.fromUUID(uuid.asJava())

}
