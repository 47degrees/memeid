package memeid.time

import java.util.concurrent.TimeUnit.MILLISECONDS

/* A posix timestamp. */
trait Posix {

  def value: Long

}

object Posix {

  implicit def apply: Posix = new Posix {
    def value: Long = MILLISECONDS toSeconds System.currentTimeMillis
  }

}
