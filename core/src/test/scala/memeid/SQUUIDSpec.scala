/*
 * Copyright 2019-2020 47 Degrees, LLC. <http://www.47deg.com>
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

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

@SuppressWarnings(Array("scalafix:Disable.map", "scalafix:Disable.to"))
class SQUUIDSpec extends Specification with ScalaCheck {

  "SQUUID constructor" should {

    "create version 4 UUIDs" in {
      val uuids = (1 to 10).par.map(_ => UUID.V4.squuid.version)

      uuids.to[Set] must contain(exactly(4))
    }

    "not generate the same UUID twice" in {
      val uuids = (1 to 10).par.map(_ => UUID.V4.squuid)

      uuids.to[Set].size must be equalTo 10
    }

  }

}
