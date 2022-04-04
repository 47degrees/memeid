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

public final class Mask {

    public static final long MASKS_56 = 0xFF00000000000000L;
    public static final long MASKS_48 = 0xFF000000000000L;

    public final static long TIME_LOW = 0xFFFFFFFFL;
    public final static long TIME_MID = 0xFFFF00000000L;
    public final static long TIME_HIGH = 0xFFF000000000000L;

    public final static long CLOCK_SEQ_LOW = 0xFFL;
    public final static long CLOCK_SEQ_HIGH = 0x3F00L;

    public final static long UB32 = 0xFFFFFFFFL;

    public final static long HASHED = 0x30000000000000L;

    public final static long V4_LSB = 0xC000000000000000L;

    public final static long VERSION = 0xF000L;
}
