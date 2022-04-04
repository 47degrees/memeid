/*
 * Copyright 2019-2022 47 Degrees Open Source <https://www.47deg.com>
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

package memeid;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Class to ensure unique gregorian timestamps. It works by keeping a sequence of
 * generated IDs and shifting the `millis` value adding the `stamp`.
 */
public final class Timestamp {

    /**
     * Returns a gregorian time monotonic timestamp.
     *
     * @return a gregorian time monotonic timestamp
     */
    public static long monotonic() {
        final State s = state.updateAndGet(state -> {
            long newMillis;
            do {
                newMillis = System.currentTimeMillis();
                if (state.millis != newMillis) {
                    return new State(0, newMillis);
                } else if (state.stamp < 999) {
                    return new State(state.stamp + 1, state.millis);
                }
            } while (true);
        });

        return s.stamp + 100103040000000000L + (1000 * (s.millis + 2208988800000L));
    }

    private static final class State {
        private final int stamp;
        private final long millis;

        private State(int stamp, long millis) {
            this.stamp = stamp;
            this.millis = millis;
        }
    }

    private final static AtomicReference<State> state = new AtomicReference<>(new State(0, 0));

}