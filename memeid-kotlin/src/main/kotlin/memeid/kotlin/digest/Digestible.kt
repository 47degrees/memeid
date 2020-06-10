package memeid.kotlin.digest

import memeid.Bits.toBytes
import memeid.UUID
import memeid.kotlin.lsb
import memeid.kotlin.msb
import java.util.function.Function
import kotlin.text.Charsets.UTF_8

/**
 * Create a wrapper to emulate type safety for two types by
 * binding the desired type to its desired type.
 */

data class Bind<Wrapper: Digestible, T>(
  val digestible: Wrapper,
  val type: T
)

typealias StringIdToByteArray = Bind<Digestible.Str, String>
typealias UuidToByteArray = Bind<Digestible.UID, UUID>
typealias CustomToByteArray<T> = Bind<Digestible.Custom<T>, T>

/**
 * Feed in type to gain access to the desired Function<T, R>
 */

open class Digestible {

  object Str: Digestible(), Transform<String> {
    override fun toByteArray() = f { t -> t.toByteArray(UTF_8) }
  }

  object UID: Digestible(), Transform<UUID> {
    override fun toByteArray() = f { t -> toBytes(t.msb).plus(toBytes(t.lsb)) }
  }

  data class Custom<T>(val function: (T) -> ByteArray): Digestible(), Transform<T> {
    override fun toByteArray() = f { t -> function(t) }
  }

  companion object {
    operator fun invoke(input: String) = StringIdToByteArray(Str, input).digestible
    operator fun invoke(input: UUID) = UuidToByteArray(UID, input).digestible
    inline operator fun <reified T> invoke(
      input: T,
      noinline function: (T) -> ByteArray
    ) = CustomToByteArray(Custom(function), input).digestible
  }
}

interface Transform<A> : memeid.kotlin.digest.Function<A> {
  fun toByteArray(): Function<A, ByteArray>
}

interface Function<A> {
  fun Transform<A>.f(f: (A) -> ByteArray): Function<A, ByteArray> = Function { t -> f(t) }
}






