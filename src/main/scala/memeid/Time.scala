package memeid.time

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

import cats.effect._

trait Time[F[_]] {
  def monotonic: F[Long]
}

object Time {

  final case class State(seq: Short, timestamp: Long) {
    def next: State = State((seq + 1).toShort, timestamp)

    def monotonic: Long =
      seq + 100103040000000000L + (1000 * (timestamp + 2208988800000L))
  }

  object State {
    val empty: State = State(0, 0)

    def update(s: State): State =
      updateTimestamp(s, System.currentTimeMillis, 999)

    @tailrec
    @SuppressWarnings(Array("scalafix:DisableSyntax.!="))
    def updateTimestamp(s: State, ts: Long, resolution: Short): State = {
      if (s.timestamp != ts) {
        s.copy(seq = 0, timestamp = ts)
      } else {
        if (s.seq < resolution) {
          s.next
        } else {
          updateTimestamp(s, System.currentTimeMillis, resolution)
        }
      }
    }
  }

  val state: AtomicReference[State] = new AtomicReference[State](State.empty)

  implicit def apply[F[_]: Sync]: Time[F] = new Time[F] {

    def monotonic: F[Long] = Sync[F].delay {
      val newState = state.updateAndGet(State.update)
      newState.monotonic
    }
  }
}
