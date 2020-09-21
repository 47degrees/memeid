package memeid4s.fuuid

import io.chrisdavenport.fuuid.FUUID
import memeid4s.UUID

object syntax {

  implicit class FUUID2UUIDOps(private val fuuid: FUUID) extends AnyVal {

    /**
     * Converts the current `FUUID` value to a memeid's `UUID`.
     */
    def toUUID: UUID = UUID.fromUUID(FUUID.Unsafe.toUUID(fuuid))

  }

  implicit class UUID2FUUIDOps(private val uuid: UUID) extends AnyVal {

    /**
     * Converts the current memeid's `UUID` value to a `FUUID`.
     */
    def toFUUID: FUUID = FUUID.fromUUID(uuid.asJava())

  }

}
