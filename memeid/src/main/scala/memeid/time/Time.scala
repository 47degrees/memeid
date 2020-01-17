/*
 * Copyright 2019-2020 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package memeid.time

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

trait Time {

  /* A gregorian time monotonic timestamp. */
  def monotonic: Long

}

@SuppressWarnings(Array("scalafix:DisableSyntax.!="))
object Time {

  def current: Long = System.currentTimeMillis

  /* Class to ensure unique gregorian timestamps for a given `resolution`. It works by keeping a sequence of
   * generated IDs and shifting the `ts` value adding the `seq`. Users of this class must ensure to
   * stall timestamp generation if the seqids are all used for the given `resolution`, since `State#update`
   * will block until it finds a different timestamp to generate new monotonic values. */
  final case class State(seq: Short, ts: Long, resolution: Short) {
    /* Advance to next state, upping the sequence number. */
    private def next: State = State((seq + 1).toShort, ts, resolution)

    /* Reset the state with a new tick. */
    private def reset(newTs: Long): State =
      copy(seq = 0, ts = newTs)

    /* Update the state with a new tick. */
    @tailrec
    def update(newTs: Long): State =
      if (ts != newTs) {
        reset(newTs)
      } else if (seq < resolution) {
        next
      } else {
        update(current)
      }

    /* Compute the gregorian time monotonic timestamp for this state. */
    def timestamp: Long =
      seq + 100103040000000000L + (1000 * (ts + 2208988800000L))
  }

  object State {
    val empty: State = State(0, 0, 999)
  }

  /* We ensure atomic updates to the state with no locks using an atomic reference, which allows
   * us to stall timestamp generation if many concurrent timestamps are generated. */
  val state: AtomicReference[State] = new AtomicReference[State](State.empty)

  implicit def apply: Time = new Time {

    override def monotonic: Long =
      state.updateAndGet(_.update(current)).timestamp

  }
}
