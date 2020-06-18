package memeid.kotlin

import memeid.UUID as MEME_ID

/** The most significant 64 bits of this UUID's 128 bit value */
val MEME_ID.msb: Long
  get() = this.mostSignificantBits

/** The least significant 64 bits of this UUID's 128 bit value */
val MEME_ID.lsb: Long
  get() = this.leastSignificantBits

