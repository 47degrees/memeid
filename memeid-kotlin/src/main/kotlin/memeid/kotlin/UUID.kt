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
import java.util.function.Function

typealias V1 = memeid.UUID.V1
typealias V2 = memeid.UUID.V2
typealias V4 = memeid.UUID.V4
typealias V5 = memeid.UUID.V5
typealias UnknownVersion = memeid.UUID.UnknownVersion

class UUID(val uuid: memeid.UUID) {
  object V1 {
    fun next(node: Node): memeid.UUID =  memeid.UUID.V1.next(node.node) { time.monotonic }
  }

  object V3 {
    inline fun <reified A: memeid.UUID> from(namespace: memeid.UUID): memeid.UUID {
      val handler = Function<A, ByteArray> { t ->
        when (t) {
          // is Digest<String> ->
        }

        byteArrayOf()
      }

      return memeid.UUID.V3.from(namespace, A::class.java, handler.apply("wha"))
    }
  }
}


