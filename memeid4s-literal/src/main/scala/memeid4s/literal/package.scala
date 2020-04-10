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

package memeid4s

package object literal {

  @SuppressWarnings(Array("scalafix:Disable.Any"))
  implicit class UUIDInterpolator(private val sc: StringContext) extends AnyVal {

    /** Validates and transforms a literal string as a valid UUID in compile time */
    def uuid(args: Any*): memeid.UUID = macro Macros.uuidInterpolator

  }

}
