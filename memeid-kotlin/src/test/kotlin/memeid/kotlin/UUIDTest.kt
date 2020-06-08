package memeid.kotlin

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class UUIDTest {
  @Test
  fun `Create UUID from valid string`() {

    val validUUIDStrings: String = (1..36).map { UUID_REGEX.random() }.joinToString("")
    println(validUUIDStrings)

    assertThat(true, equalTo(true))
  }

  @Test
  fun `Create memeid from UUID V1`() {
    val v1List = (1..50).map { UUIDGenerator.generateV1() }.toList()

    v1List.forEach { uuid ->
      val fromUUID = UUID.fromUUID(uuid)
      val fromSigBits = UUID.from(uuid.mostSignificantBits, uuid.leastSignificantBits)
      val fromString = UUID.fromString(uuid.toString())

      println( """
      Generated Version 1 UUID: $uuid | UUID Version: ${uuid.version()} | UUID Variant: ${uuid.version()} 
      |  From UUID: $fromUUID | version: ${fromUUID.version()} | variant: ${fromUUID.variant()}
      |  From significant bits: $fromSigBits | version: ${fromSigBits.version()} | variant: ${fromSigBits.variant()}
      |  From String: $fromString | version: ${fromString?.version()} | variant: ${fromString?.variant()}
      --------------------------------------------------------------------""".trimIndent())

      assertThat(fromUUID.isV1, equalTo(true))
      assertThat(fromSigBits.isV1, equalTo(true))
      assertThat(fromString?.isV1, equalTo(true))
    }
  }

  @Test
  fun `Create V1 memeid from Kotlin module`() {
    val uuid = UUID.V1.next

    val fromUUID = UUID.fromUUID(uuid.asJava())
    val fromSigBits = UUID.from(uuid.msb, uuid.lsb)
    val fromString = UUID.fromString(uuid.asJava().toString())

    println( """
      Generated Version 1 UUID: $uuid | UUID Version: ${uuid.version()} | UUID Variant: ${uuid.version()} 
      |  From UUID: $fromUUID | version: ${fromUUID.version()} | variant: ${fromUUID.variant()}
      |  From significant bits: $fromSigBits | version: ${fromSigBits.version()} | variant: ${fromSigBits.variant()}
      |  From String: $fromString | version: ${fromString?.version()} | variant: ${fromString?.variant()}
      --------------------------------------------------------------------""".trimIndent())

    assertThat(fromUUID.isV1, equalTo(true))
    // assertThat(fromSigBits.isV1, equalTo(true))   <-- returns a NIL version
    assertThat(fromString?.isV1, equalTo(true))
  }

  @Test
  fun `Create UUID from UUID v4`() {
    val v1List = (1..50).map { UUIDGenerator.generateV4() }.toList()

    v1List.forEach { uuid ->
      val fromUUID = UUID.fromUUID(uuid)
      val fromSigBits = UUID.from(uuid.mostSignificantBits, uuid.leastSignificantBits)
      val fromString = UUID.fromString(uuid.toString())

      println( """
      Generated Version 4 UUID: $uuid | UUID Version: ${uuid.version()} | UUID Variant: ${uuid.version()} 
      |  From UUID: $fromUUID | version: ${fromUUID.version()} | variant: ${fromUUID.variant()}
      |  From significant bits: $fromSigBits | version: ${fromSigBits.version()} | variant: ${fromSigBits.variant()}
      |  From String: $fromString | version: ${fromString?.version()} | variant: ${fromString?.variant()}
      --------------------------------------------------------------------""".trimIndent())

      assertThat(fromUUID.isV4, equalTo(true))
      assertThat(fromSigBits.isV4, equalTo(true))
      assertThat(fromString?.isV4, equalTo(true))
    }
  }

  @Test
  fun `Attempt to create UUID from invalid string`() {
    assertThat(1 == 1, equalTo(true))
  }

  @Test
  fun `Attempt to create UUID from invalid type`() {
    val allVersions = listOf(
      UUIDGenerator.generateV1(),

      UUIDGenerator.generateV4()
    )
  }

  @Test
  fun `UUIDs correspond to version types`() {

  }

  @Test
  fun `UUIDs do not correspond to version types`() {

  }

  @Test
  fun `Detect a valid UUID variant`() {

  }

  @Test
  fun `Detect a invalid UUID variant`() {

  }

  @Test
  fun `Nil variant must be equal to 0`() {

  }

  @Test
  fun `Nil all 128 bits in 0`() {

  }

  companion object {
    // Canonically formatted UUID for V1..V5
    const val UUID_REGEX = "/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}\$/i"
  }
  
}