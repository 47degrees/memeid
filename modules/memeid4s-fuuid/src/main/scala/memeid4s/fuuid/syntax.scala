/*
 * Copyright 2019-2023 47 Degrees Open Source <https://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package memeid4s.fuuid

import io.chrisdavenport.fuuid.FUUID
import memeid4s.UUID

object syntax {

  implicit class FUUID2UUIDOps(private val fuuid: FUUID) extends AnyVal {

    /** Converts the current `FUUID` value to a memeid's `UUID`. */
    def toUUID: UUID = UUID.fromUUID(FUUID.Unsafe.toUUID(fuuid))

  }

  implicit class UUID2FUUIDOps(private val uuid: UUID) extends AnyVal {

    /** Converts the current memeid's `UUID` value to a `FUUID`. */
    def toFUUID: FUUID = FUUID.fromUUID(uuid.asJava())

  }

}
