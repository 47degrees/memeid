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

package memeid4s.http4s

import cats.syntax.either._

import memeid4s.UUID
import org.http4s.ParseFailure
import org.http4s.QueryParamDecoder
import org.http4s.QueryParamEncoder

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract", "scalafix:Disable.toString"))
trait instances {

  /** Allow reading UUIDs from a request's query params */
  implicit val UUIDQueryParamDecoderInstance: QueryParamDecoder[UUID] =
    QueryParamDecoder[String].emap { s =>
      UUID.from(s).leftMap { e =>
        ParseFailure(s"Failed to decode value: $s as UUID", e.getMessage)
      }
    }

  /** Allow using UUIDs in a request's query params */
  implicit val UUIDQueryParamEncoderInstance: QueryParamEncoder[UUID] =
    QueryParamEncoder[String].contramap(_.toString)

}

object instances extends instances
