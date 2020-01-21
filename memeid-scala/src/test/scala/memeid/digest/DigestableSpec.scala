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

package memeid.digest

import org.specs2.mutable.Specification

class DigestableSpec extends Specification {

  "Digestible[String]" should {

    "give the same bytes for the same string" in {
      val str = "a-thing"

      Digestible[String].toByteArray(str) must be equalTo Digestible[String].toByteArray(str)
    }

  }

}
