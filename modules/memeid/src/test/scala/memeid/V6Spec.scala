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
class V6Spec extends Specification {

  "V6 constructor" should {

    "create version 6 UUIDs" in {
      val ids = new ParRange(1 to 10).map(_ => UUID.V6.next.version).toVector.toSet

      ids must contain(exactly(6))
    }

    "create monotonically increasing UUIDs" in {
      val uuid1: UUID = UUID.V6.next
      val uuid2: UUID = UUID.V6.next

      uuid1 must be lessThan uuid2
    }

    "not generate the same UUID twice" in {
      val ids = new ParRange(1 to 10).map(_ => UUID.V6.next).toSet

      ids.size must be equalTo 10
    }

    "not generate the same UUID twice with high concurrency" in {
      val ids = new ParRange(1 to 999).map(_ => UUID.V6.next).toSet

      ids.size must be equalTo 999
    }

  }

}
