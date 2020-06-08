/*
 * Copyright 2019-2020 47 Degrees Open Source <https://www.47deg.com>
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

import io.circe.testing.CodecTests
import io.circe.testing.instances._
import memeid4s.UUID
import memeid4s.cats.instances._
import memeid4s.circe.instances._
import memeid4s.scalacheck.arbitrary.instances._
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline

class UUIDCodecLaws extends Specification with Discipline {

  checkAll("UUID", CodecTests[UUID].codec)
  checkAll("UUID", CodecTests[UUID].unserializableCodec)

}
