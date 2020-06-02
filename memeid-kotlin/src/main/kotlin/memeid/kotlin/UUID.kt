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

import memeid.kotlin.node.Node
import memeid.kotlin.time.time
import memeid.UUID

typealias V1 = UUID.V1
typealias V2 = UUID.V2
typealias V3 = UUID.V3
typealias V4 = UUID.V4
typealias V5 = UUID.V5
typealias UnknownVersion = UUID.UnknownVersion

class UUID(val uuid: UUID) {

  /** The most significant 64 bits of this UUID's 128 bit value */
  val msb: Long = uuid.mostSignificantBits

  /** The least significant 64 bits of this UUID's 128 bit value */
  val lsb: Long = uuid.leastSignificantBits

  object V1 {
    fun next(node: Node): UUID =  UUID.V1.next(node.node) { time.monotonic }
  }

  object V3 {
    inline fun <reified A> apply(namespace: UUID): UUID = V3.from(namespace, A::class.java, A::scope)

  }
}


