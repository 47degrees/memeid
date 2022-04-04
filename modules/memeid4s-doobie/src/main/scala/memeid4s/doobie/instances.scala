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

package memeid4s.doobie

import java.util.{UUID => JUUID}

import doobie.util.Get
import doobie.util.Put
import memeid4s.UUID

trait instances {

  implicit def UUIDGetInstance(implicit G: Get[JUUID]): Get[UUID] =
    G.tmap(UUID.fromUUID)

  implicit def UUIDPutInstance(implicit G: Put[JUUID]): Put[UUID] =
    G.tcontramap(_.asJava)

}

object instances extends instances
