package memeid.kotlin

import memeid.UUID

/** The most significant 64 bits of this UUID's 128 bit value */
val UUID.msb: Long
  get() = this.mostSignificantBits

/** The least significant 64 bits of this UUID's 128 bit value */
val UUID.lsb: Long
  get() = this.leastSignificantBits