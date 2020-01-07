/*
 * Copyright 2019-2020 47 Degrees, LLC. <http://www.47deg.com>
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

package memeid

import java.util.{UUID => JUUID}

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

  /**
   * [[java.util.UUID]] to [[memeid.UUID]] converter
   * @param juuid [[java.util.UUID]] to be converted
   */
  implicit final class JUUIDAsScala(private val juuid: JUUID) extends AnyVal {

    /**
     * Converts this [[java.util.UUID]] into a [[memeid.UUID]]
     * @return the [[java.util.UUID]] converted into a [[memeid.UUID]]
     */
    def asScala: UUID = juuid -> juuid.version() match {
      case (Nil.juuid, 0) => Nil
      case (_, 1)         => new V1(juuid)
      case (_, 2)         => new V2(juuid)
      case (_, 3)         => new V3(juuid)
      case (_, 4)         => new V4(juuid)
      case (_, 5)         => new V5(juuid)
      case (_, _)         => new UnknownVersion(juuid)
    }

  }

  /**
   * [[memeid.UUID]] to [[java.util.UUID]] converter
   * @param uuid [[memeid.UUID]] to be converted
   */
  implicit final class UUIDAsJava(private val uuid: UUID) extends AnyVal {

    /**
     * Converts this [[memeid.UUID]] into a [[java.util.UUID]]
     * @return this [[memeid.UUID]] converted into a [[java.util.UUID]]
     */
    def asJava: JUUID = uuid.juuid

  }

}
