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
import memeid.kotlin.UUID.V3.invoke
import scala.Predef
import scala.inline
import scala.runtime.BoxesRunTime.toByte
import java.util.function.Function
import kotlin.reflect.KClass
import kotlin.text.Charsets.UTF_8

/**
 * Create a wrapper to emulate type safety for two types by
 * binding the desired type to its desired type.
 */

data class Bind<Wrapper: Digestible, T>(
  val type: T
)

typealias StringIdToByteArray = Bind<Digestible.Str, String>
typealias UuidToByteArray = Bind<Digestible.UID, UUID>

/**
 * Feed in type to gain access to the desired Function<T, R>
 */

sealed class Digestible {

  data class Str(val kotlinClass: KClass<String>): Digestible(), Transform<String> {
    override val toByteArray = f { t -> t.toByteArray(UTF_8) }
  }

  data class UID(val kotlinClass: KClass<UUID>): Digestible(), Transform<UUID> {
    override val toByteArray = f { t -> toBytes(t.msb).plus(toBytes(t.lsb)) }
  }

  operator fun invoke(id: StringIdToByteArray) = Str(String::class)
  operator fun invoke(id: UuidToByteArray) = UID(UUID::class)
}

interface Transform<A> : memeid.kotlin.Function<A> {
  val toByteArray: Function<A, ByteArray>
}

interface Function<A> {
  fun Transform<A>.f(f: (A) -> ByteArray): Function<A, ByteArray> = Function { t -> f(t) }
}






