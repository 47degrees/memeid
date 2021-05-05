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

package memeid4s.digest

import java.nio.charset.StandardCharsets.UTF_8

import memeid.Bits.toBytes
import memeid4s.UUID
import memeid4s.UUID.RichUUID

trait Digestible[A] { self =>

  def toByteArray(a: A): Array[Byte]

}

object Digestible {

  def apply[A](implicit d: Digestible[A]): Digestible[A] = d

  implicit val DigestibleStringImplementation: Digestible[String] = _.getBytes(UTF_8)
  implicit val DigestibleUUIDInstance: Digestible[UUID]           = u => toBytes(u.msb) ++ toBytes(u.lsb)

}
