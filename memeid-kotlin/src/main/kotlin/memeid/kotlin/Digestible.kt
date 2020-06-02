package memeid.kotlin

import memeid.Bits.toBytes
import memeid.kotlin.Digestable.toByteArray
import java.util.function.Function
import kotlin.text.Charsets.UTF_8

object Digestable {
  @JvmStatic fun <T, R> toByteArray(function: Function<T, R>): (T) -> R = function::apply

  fun call(function: (String) -> ByteArray, arg: String): ByteArray = function(arg)
  fun call(function: Function<String, ByteArray>, arg: String): ByteArray = call(toByteArray(function), arg)

  fun call(function: (UUID) -> ByteArray, arg: UUID): ByteArray = function(arg)
  fun call(function: Function<UUID, ByteArray>, arg: UUID): ByteArray = call(toByteArray(function), arg)
}

val string = Function<String, ByteArray> { t -> t.toByteArray(UTF_8) }
val uuid = Function<UUID, ByteArray> { t -> toBytes(t.msb).plus(toBytes(t.lsb))}



