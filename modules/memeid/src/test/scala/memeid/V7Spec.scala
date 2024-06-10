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

package memeid

import scala.collection.parallel.immutable.ParRange

import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.scala.collection.parallel"))
class V7Spec extends Specification {

  "V7 constructor" should {

    "create version 7 UUIDs" in {
      val uuids = new ParRange(1 to 10).map(_ => UUID.V7.next.version).toVector.toSet

      uuids must contain(exactly(7))
    }

    "not generate the same UUID twice" in {
      val uuids = new ParRange(1 to 10).map(_ => UUID.V7.next).toVector.toSet

      uuids.size must be equalTo 10
    }

    "generate version 7 UUIDs regardless of timestamp value provided" in {
      val nonNull: UUID = UUID.V7.next(0)
      nonNull.version must be equalTo 7
    }

    "generate a version 7 UUID with the proper timestamp" in {
      val uuid: UUID.V7 = UUID.V7.next(-1).asV7().get()
      uuid.timestamp must be equalTo 281474976710655L
    }
  }

}
