package memeid.kotlin.time

import memeid.Timestamp

interface Time {
  val monotonic: Long
}

val time: Time by lazy {
  object : Time {
    override val monotonic: Long
      get() = Timestamp.monotonic()
  }
}