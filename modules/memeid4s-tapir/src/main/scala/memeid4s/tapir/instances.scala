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

package memeid4s.tapir

import memeid4s.UUID
import sttp.tapir.Codec
import sttp.tapir.CodecFormat
import sttp.tapir.DecodeResult
import sttp.tapir.Schema

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait instances {

  /** Allows reading `UUID` values from path, query params, or headers */
  implicit val UUIDCodec: Codec[String, UUID, CodecFormat.TextPlain] =
    Codec.string
      .mapDecode(s => UUID.from(s).fold(DecodeResult.Error(s, _), DecodeResult.Value(_)))(_.toString) // scalafix:ok
      .schema(_.format("uuid"))

  /** Provides a valid `Schema` for `UUID` type, to be used when generating documentation. */
  implicit val UUIDSchema: Schema[UUID] = Schema.string[UUID].format("uuid")

}

object instances extends instances
