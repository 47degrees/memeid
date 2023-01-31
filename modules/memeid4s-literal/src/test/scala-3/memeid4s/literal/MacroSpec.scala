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

package memeid4s.literal

class MacroSpec extends munit.FunSuite {

  test("fail on using variables") {
    val message = "error: uuid interpolator should only be used on string literals"
    val errors = compileErrors("""
        val part = "06a9"
        uuid"be5cb243-$part-409e-899f-109d0ed8ea01"
        """)
    assert(
      errors.contains(message),
      s"""
         |$errors
         |doesn't contain
         |$message
         |""".stripMargin
    )
  }

}
