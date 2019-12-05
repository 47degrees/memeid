package memeid.time

import cats.effect._

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

trait Time[F[_]] {
  def monotonic: F[Long]
}

object Time {
  case class State(seq: Short, timestamp: Long) {
    def next: State = State((seq + 1).toShort, timestamp)

    def monotonic: Long =
      seq + 100103040000000000L + (1000 * (timestamp + 2208988800000L))    
  }

  object State {
    val empty: State = State(0, 0)

    def update(s: State): State =
      updateTimestamp(s, System.currentTimeMillis, 999)

    @tailrec
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

  val state = new AtomicReference[State](State.empty)

  implicit def apply[F[_] : Sync]: Time[F] = new Time[F] {
    def monotonic: F[Long] = Sync[F].delay {
      val newState = state.updateAndGet(State.update)
      newState.monotonic
    }
  }
}
