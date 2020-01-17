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

package memeid.http4s

import cats.syntax.show._

import memeid.UUID
import memeid.cats.instances._
import memeid.http4s.instances._
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.{QueryParamEncoder, QueryParameterValue}
import org.scalacheck.Gen
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class InstancesSpec extends Specification with ScalaCheck {

  object QueryParamMatcher extends QueryParamDecoderMatcher[UUID]("miau")

  "QueryParamDecoder[UUID]" should {

    "correctly decode a valid UUID" in prop { uuid: UUID =>
      val queryParams = Map("miau" -> List(uuid.show))

      QueryParamMatcher.unapply(queryParams) must be some uuid
    }

    "fail given an invalid UUID" in prop { string: String =>
      val queryParams = Map("miau" -> List(string))

      QueryParamMatcher.unapply(queryParams) must beNone
    }.setGen(Gen.alphaNumStr)

  }

  "QueryParamEncoder[UUID]" should {

    "correctly encode a valid UUID" in prop { uuid: UUID =>
      QueryParamEncoder[UUID].encode(uuid) must be like {
        case QueryParameterValue(string) => string must be equalTo uuid.show
      }
    }

  }

}
