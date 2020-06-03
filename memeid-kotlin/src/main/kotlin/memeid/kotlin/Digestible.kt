package memeid.kotlin
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

import memeid.Bits.toBytes
import java.util.function.Function
import kotlin.text.Charsets.UTF_8

interface Format<A> : memeid.kotlin.Function<A>

interface Function<A> {
  fun Format<A>.f(f: (A) -> ByteArray): Function<A, ByteArray> = Function { t -> f(t) }
}

fun <A, B> scope(
  env: Format<A>.() -> B
): B = env(object : Format<A> {})

object Digestible {
  operator fun invoke(string: String): Function<String, ByteArray> = scope { f { string.toByteArray(UTF_8)} }
  operator fun invoke(uuid: UUID): Function<UUID, ByteArray> = scope { f { toBytes(uuid.msb) + toBytes(uuid.lsb) } }
}




