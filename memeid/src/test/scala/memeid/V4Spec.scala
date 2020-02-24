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

package memeid

import scala.collection.parallel.immutable.ParRange

import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.scala.parallel"))
class V4Spec extends Specification {

  "V4 constructor" should {

    "create version 4 UUIDs" in {
      val uuids = new ParRange(1 to 10).map(_ => UUID.V4.random.version).toVector.toSet

      uuids must contain(exactly(4))
    }

    "not generate the same UUID twice" in {
      val uuids = new ParRange(1 to 10).map(_ => UUID.V4.random).toVector.toSet

      uuids.size must be equalTo 10
    }

    "be unable to create non-v4 values regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.V4.from(0, 0)
      nonNull must not be equalTo(UUID.NIL)
    }

    "generate version 4 UUIDs regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.V4.from(0, 0)
      nonNull.version must be equalTo 4
    }

  }

}
