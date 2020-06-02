package memeid.kotlin

import memeid.Bits.toBytes
import java.util.function.Function
import kotlin.text.Charsets.UTF_8

object Digestable {
  @JvmStatic fun <T, R> toByteArray(function: Function<T, R>): (T) -> R = function::apply

  fun call(function: (String) -> ByteArray, arg: String): ByteArray = function(arg)
  fun call(function: Function<String, ByteArray>, arg: String): ByteArray = call(toByteArray(function), arg)

  fun call(function: (UUID) -> ByteArray, arg: UUID): ByteArray = function(arg)
  fun call(function: Function<UUID, ByteArray>, arg: UUID): ByteArray = call(toByteArray(function), arg)
}

interface Digestible : FormatString, FormatUuid

interface FormatString {
  fun Digestible.f(
    f: (String) -> ByteArray
  ): Function<String, ByteArray> = Function { t -> f(t) }
}

interface FormatUuid {
  fun Digestible.g(
    f: (UUID) -> ByteArray
  ): Function<UUID, ByteArray> = Function { t -> f(t) }
}

fun <A> scope(
  env: Digestible.() -> A
): A = env(object : Digestible {})

val digestible: Function<UUID, ByteArray> =
  scope {
    f { string -> string.toByteArray(UTF_8) }
    g { uuid -> toBytes(uuid.msb).plus(toBytes(uuid.lsb)) }
  }



