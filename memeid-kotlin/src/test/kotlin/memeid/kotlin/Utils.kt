package memeid.kotlin

import com.fasterxml.uuid.Generators
import java.util.UUID

object UUIDGenerator {

  /***** Generate Version 1 UUID - Time-Based UUID  */
  fun generateV1(): UUID = Generators.timeBasedGenerator().generate()

  /***** Generate Version 4 UUID - Randomly Generated UUID  */
  fun generateV4(): UUID = Generators.randomBasedGenerator().generate()
}

data class Person(val firstName: String, val lastName: String)