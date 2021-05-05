/*
 * Copyright 2019-2021 47 Degrees Open Source <https://www.47deg.com>
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

package memeid4s.circe

import cats.syntax.show._

import io.circe.Decoder
import io.circe.Encoder
import io.circe.Json
import memeid4s.UUID
import memeid4s.cats.instances._
import memeid4s.circe.instances._
import memeid4s.scalacheck.arbitrary.instances._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class CirceInstancesSpec extends Specification with ScalaCheck {

  "Encoder[UUID]" should {

    "encode UUID as JSON" in prop { uuid: UUID =>
      Encoder[UUID].apply(uuid) must be equalTo Json.fromString(uuid.show)
    }

  }

  "Decoder[UUID]" should {

    "decode JSON as UUID" in prop { uuid: UUID =>
      val json = Json.fromString(uuid.show)
      Decoder[UUID].decodeJson(json) must beRight(uuid)
    }

  }

}
