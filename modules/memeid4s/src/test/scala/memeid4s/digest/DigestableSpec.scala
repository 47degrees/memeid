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

package memeid4s.digest

import java.nio.ByteBuffer

import memeid4s.UUID
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class DigestableSpec extends Specification with ScalaCheck {

  "Digestible[String]" should {

    "give the same bytes for the same string" in {
      val str = "a-thing"

      Digestible[String].toByteArray(str) must be equalTo Digestible[String].toByteArray(str)
    }

  }

  "Digestible[UUID]" should {

    def fromByteArray(bytes: Array[Byte]): UUID = {
      val bb = ByteBuffer.wrap(bytes)
      UUID.from(bb.getLong, bb.getLong)
    }

    "round-trip" in prop { (msb: Long, lsb: Long) =>
      val uuid  = UUID.from(msb, lsb)
      val bytes = Digestible[UUID].toByteArray(uuid)
      fromByteArray(bytes) must be equalTo uuid
    }

    "give the same bytes for the same UUID" in {
      val uuid = UUID.V1.next

      Digestible[UUID].toByteArray(uuid) must be equalTo Digestible[UUID].toByteArray(uuid)
    }

  }

}
