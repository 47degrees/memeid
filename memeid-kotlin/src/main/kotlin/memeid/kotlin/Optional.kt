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

package memeid.kotlin

import memeid.UUID
import java.util.NoSuchElementException

sealed class Optional<T>(private val value: T?) {

  abstract val isEmpty: Boolean

  fun get(): T {
    if (value == null) throw NoSuchElementException("No value present")
    return value
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false

    val optional1 = other as Optional<*>?

    return if (value != null) value == optional1!!.value else optional1!!.value == null
  }

  override fun hashCode(): Int = value?.hashCode() ?: 0
}

data class Some<T>(val value: T?): Optional<T>(value) {
  override val isEmpty: Boolean
    get() = true
}

class None<T>: Optional<T>(null) {
  override val isEmpty: Boolean
    get() = false
}