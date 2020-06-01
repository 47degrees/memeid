package memeid.kotlin

import memeid.Bits.toBytes
import java.nio.charset.StandardCharsets.UTF_8
import java.util.function.Function

interface Digest<A> {
  fun toByteArray(a: A): Optional<ByteArray>
}

// val digestible: Function<String, ByteArray> = x -> x.toByteArray(UTF_8)



