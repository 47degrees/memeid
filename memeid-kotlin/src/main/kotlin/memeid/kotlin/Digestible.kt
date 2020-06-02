package memeid.kotlin

import memeid.Bits.toBytes
import java.util.function.Function
import kotlin.text.Charsets.UTF_8

interface Format : FormatString, FormatUuid

interface FormatString {
  fun Format.f(
    f: (String) -> ByteArray
  ): Function<String, ByteArray> = Function { t -> f(t) }
}

interface FormatUuid {
  fun Format.g(
    f: (UUID) -> ByteArray
  ): Function<UUID, ByteArray> = Function { t -> f(t) }
}

fun <A> scope(
  env: Format.() -> A
): A = env(object : Format {})

object Digestible {
  

  operator fun invoke(string: String): Function<String, ByteArray> = scope { f { string.toByteArray(UTF_8)} }
  operator fun invoke(uuid: UUID): Function<UUID, ByteArray> = scope { g { toBytes(uuid.msb) + toBytes(uuid.lsb) } }
}

fun String.scope() = Digestible.invoke(this)
fun UUID.scope() = Digestible.invoke(this)



