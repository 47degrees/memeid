/*
 * Copyright 2019-2020 47 Degrees Open Source <http://47deg.com>
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

import java.util.{UUID => JUUID}

import io.circe.{Decoder, Encoder}
import memeid4s.UUID

trait instances {

  implicit def UUIDEncoderInstance(implicit E: Encoder[JUUID]): Encoder[UUID] =
    E.contramap(_.asJava)

  implicit def UUIDDecoderInstance(implicit D: Decoder[JUUID]): Decoder[UUID] =
    D.map(UUID.fromUUID)

}

object instances extends instances
