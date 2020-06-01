package memeid.kotlin.time

import java.util.concurrent.TimeUnit

interface Posix {
  val value: Long
}

val posix: Posix by lazy {
  object : Posix {
    override val value: Long
      get() = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
  }
}