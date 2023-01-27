/*
 * Copyright 2019-2022 47 Degrees Open Source <https://www.47deg.com>
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

package memeid4s.tapir

import io.chrisdavenport.fuuid.FUUID
import memeid4s.UUID
import memeid4s.fuuid.syntax._
import memeid4s.scalacheck.arbitrary.instances._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class SynxtaxSpec extends Specification with ScalaCheck {

  "UUID-FUUID conversions" should {

    "convert between UUID & FUUID" in prop { (uuid: UUID) =>
      uuid.toFUUID must be equalTo FUUID.fromUUID(uuid.asJava())
    }

    "convert between FUUID & UUID" in prop { (uuid: UUID) =>
      val fuuid = uuid.toFUUID

      fuuid.toUUID must be equalTo uuid
    }

  }

}
