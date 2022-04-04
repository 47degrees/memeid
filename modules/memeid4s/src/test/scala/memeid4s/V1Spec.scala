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

package memeid4s

import scala.collection.parallel.immutable.ParRange

import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.scala.collection.parallel"))
class V1Spec extends Specification {

  "V1 constructor" should {

    "create version 1 UUIDs" in {
      val ids = new ParRange(1 to 10).map(_ => UUID.V1.next.version).toVector.toSet

      ids must contain(exactly(1))
    }

    "create monotonically increasing UUIDs" in {
      val uuid1: UUID = UUID.V1.next
      val uuid2: UUID = UUID.V1.next

      uuid1 must be lessThan uuid2
    }

    "not generate the same UUID twice" in {
      val ids = new ParRange(1 to 10).map(_ => UUID.V1.next).toSet

      ids.size must be equalTo 10
    }

    "not generate the same UUID twice with high concurrency" in {
      val ids = new ParRange(1 to 999).map(_ => UUID.V1.next).toSet

      ids.size must be equalTo 999
    }

    "check time components" in {
      val uuid         = memeid.UUID.fromString("1cbf0782-3209-11ea-978f-2e728ce88125")
      val clockSeqLow  = 0x8f.toLong
      val clockSeqHigh = 0x17.toLong // 0x97 - 0x80 (from variant)
      val timeLow      = 0x1cbf0782.toLong
      val timeMid      = 0x3209.toLong
      val timeHigh     = 0x1ea.toLong
      val uuidV1       = uuid.asV1.get

      uuidV1.clockSeqLow must be equalTo clockSeqLow
      uuidV1.clockSeqHigh must be equalTo clockSeqHigh
      uuidV1.timeLow must be equalTo timeLow
      uuidV1.timeMid must be equalTo timeMid
      uuidV1.timeHigh must be equalTo timeHigh
    }
  }

}
