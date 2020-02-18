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

package memeid4s

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.scala.parallel"))
class V4Spec extends Specification with ScalaCheck {

  "V4 constructor" should {

    "create version 4 UUIDs" in {
      val uuids = (1 to 10).par.map(_ => UUID.V4.random.version)

      uuids.to[Set] must contain(exactly(4))
    }

    "not generate the same UUID twice" in {
      val uuids = (1 to 10).par.map(_ => UUID.V4.random)

      uuids.to[Set].size must be equalTo 10
    }

    "be unable to create non-v4 values regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.V4(0, 0)
      nonNull must not be equalTo(UUID.Nil)
    }

    "generate version 4 UUIDs regardless of msb/lsb values provided" in {
      val nonNull: UUID = UUID.V4(0, 0)
      nonNull.version must be equalTo 4
    }

  }

}
