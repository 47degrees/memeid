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

package memeid4s.tapir

import cats.syntax.show._

import memeid4s.UUID
import memeid4s.cats.implicits._
import memeid4s.scalacheck.arbitrary.instances._
import memeid4s.tapir.implicits._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import sttp.apispec.openapi.circe.yaml._
import sttp.tapir._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter

class InstancesSpec extends Specification with ScalaCheck {

  "Schema[UUID] & Codec[UUID]" should {

    "provide correct schema when generating documentation" in {
      val testEndpoint = endpoint.get.in("hello" / path[UUID])

      val result = OpenAPIDocsInterpreter().toOpenAPI(testEndpoint, "", "").toYaml

      val expected =
        """openapi: 3.0.3
          |info:
          |  title: ''
          |  version: ''
          |paths:
          |  /hello/{p1}:
          |    get:
          |      operationId: getHelloP1
          |      parameters:
          |      - name: p1
          |        in: path
          |        required: true
          |        schema:
          |          type: string
          |          format: uuid
          |      responses:
          |        '200':
          |          description: ''
          |        '400':
          |          description: 'Invalid value for: path parameter p1'
          |          content:
          |            text/plain:
          |              schema:
          |                type: string
          |""".stripMargin

      result must be equalTo expected
    }

    "validates a valid UUID" in prop { (uuid: UUID) =>
      val result = UUIDCodec.decode(uuid.show)

      result must be equalTo DecodeResult.Value(uuid)
    }

    "validates an invalid UUID" in {
      val result = UUIDCodec.decode("miau")

      result must be like { case DecodeResult.Error("miau", e: IllegalArgumentException) =>
        e.getMessage() must be equalTo "Invalid UUID string: miau"
      }
    }

  }

}
