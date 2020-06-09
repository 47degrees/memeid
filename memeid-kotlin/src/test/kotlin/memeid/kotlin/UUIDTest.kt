package memeid.kotlin

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class UUIDTest {

  @Test
  fun `Create memeid from UUID V1`() {
    val v1List = (1..50).map { UUIDGenerator.generateV1() }.toList()

    v1List.forEach { uuid ->
      val fromUUID = UUID.fromUUID(uuid)
      val fromSigBits = UUID.from(uuid.mostSignificantBits, uuid.leastSignificantBits)
      val fromString = UUID.fromString(uuid.toString())

      logTest(1, uuid, fromUUID, fromSigBits, fromString)

      assertThat(fromUUID.isV1, equalTo(true))
      assertThat(fromSigBits.isV1, equalTo(true))
      assertThat(fromString.isV1, equalTo(true))
    }
  }

  @Test
  fun `Create V1 memeid with memeid-kotlin`() {
    val uuid = UUID.V1.next

    val fromUUID = UUID.fromUUID(uuid.asJava())
    val fromSigBits = UUID.from(uuid.msb, uuid.lsb)
    val fromString = UUID.fromString(uuid.asJava().toString())

    assertThat(fromUUID.isV1, equalTo(true))
    assertThat(fromSigBits.isV1, equalTo(true))
    assertThat(fromString.isV1, equalTo(true))
  }

  @Test
  fun `Create V3 memeid namespace with String`() {
    val uuid = UUID.V1.next
    val fromUUID = UUID.V3(uuid, "namespace-test")

    assertThat(fromUUID.isV3, equalTo(true))
  }

  @Test
  fun `Create V3 memeid namespace with UUID`() {
    val a = UUID.V1.next
    val b = UUID.V4.random

    listOf(
      Pair(a, b),
      Pair(a, a),
      Pair(b, a)
    ).forEach { pair ->
      val uuid = UUID.V3(pair.first, pair.second)
      assertThat(uuid.isV3, equalTo(true))
    }
  }

  @Test
  fun `Create V3 memeid namespace with custom Digestible`() {
    val uuid = UUID.V1.next
    val person = Person("Federico", "García Lorca")

    val fromUUID = UUID.V3(uuid, person) {
      it.firstName.toByteArray() + it.lastName.toByteArray()
    }

    assertThat(fromUUID.isV3, equalTo(true))
  }

  @Test
  fun `Create UUID from UUID v4`() {
    val v1List = (1..50).map { UUIDGenerator.generateV4() }.toList()

    v1List.forEach { uuid ->
      val fromUUID = UUID.fromUUID(uuid)
      val fromSigBits = UUID.from(uuid.mostSignificantBits, uuid.leastSignificantBits)
      val fromString = UUID.fromString(uuid.toString())

      logTest(4, uuid, fromUUID, fromSigBits, fromString)

      assertThat(fromUUID.isV4, equalTo(true))
      assertThat(fromSigBits.isV4, equalTo(true))
      assertThat(fromString.isV4, equalTo(true))
    }
  }

  @Test
  fun `Create V4 memeid with memeid-kotlin`() {
    listOf(UUID.V4.random, UUID.V4.squuid).forEach { uuid ->
      val fromUUID = UUID.fromUUID(uuid.asJava())
      val fromSigBits = UUID.from(uuid.msb, uuid.lsb)
      val fromString = UUID.fromString(uuid.asJava().toString())

      logTest(uuid, fromUUID, fromSigBits, fromString)

      assertThat(fromUUID.isV4, equalTo(true))
      assertThat(fromSigBits.isV4, equalTo(true))
      assertThat(fromString.isV4, equalTo(true))
    }
  }
  @Test
  fun `Create V5 memeid namespace with String`() {
    val uuid = UUID.V1.next
    val fromUUID = UUID.V5(uuid, "namespace-test")

    assertThat(fromUUID.isV5, equalTo(true))
  }

  @Test
  fun `Create V5 memeid namespace with UUID`() {
    val a = UUID.V1.next
    val b = UUID.V4.random

    listOf(
      Pair(a, b),
      Pair(a, a),
      Pair(b, a)
    ).forEach { pair ->
      val uuid = UUID.V5(pair.first, pair.second)
      assertThat(uuid.isV5, equalTo(true))
    }
  }

  @Test
  fun `Create V5 memeid namespace with custom Digestible`() {
    val uuid = UUID.V1.next
    val person = Person("Federico", "García Lorca")

    val fromUUID = UUID.V5(uuid, person) {
      it.firstName.toByteArray() + it.lastName.toByteArray()
    }

    assertThat(fromUUID.isV5, equalTo(true))
  }

  @Test
  fun `Attempt to create UUID from invalid string`() {
    val uuidString = "1-1-1-1-1"
    val fromString = UUID.fromString(uuidString)

    // Catches an illegal argument exception but returns null
    assertThat(fromString.isNil, equalTo(true))
  }

  @Test
  fun `Attempt to create UUID from invalid long bytes`() {
    val fromString = UUID.from(0L, 1000L)

    assertThat(fromString.version(), equalTo(0))
  }

  @Test
  fun `Nil variant must be equal to 0`() {
    assertThat(memeid.UUID.NIL.variant(), equalTo(0))
  }

  @Test
  fun `Nil all 128 bits in 0`() {
    assertThat(memeid.UUID.NIL.mostSignificantBits, equalTo(0L))
    assertThat(memeid.UUID.NIL.leastSignificantBits, equalTo(0L))
  }

  private fun logTest(uuid: memeid.UUID, fromUUID: memeid.UUID, fromSigBits: memeid.UUID, fromString: memeid.UUID?) {
    println( """
      Generated Version 4 UUID: $uuid | UUID Version: ${uuid.version()} | UUID Variant: ${uuid.version()} 
      |  From UUID: $fromUUID | version: ${fromUUID.version()} | variant: ${fromUUID.variant()}
      |  From significant bits: $fromSigBits | version: ${fromSigBits.version()} | variant: ${fromSigBits.variant()}
      |  From String: $fromString | version: ${fromString?.version()} | variant: ${fromString?.variant()}
      --------------------------------------------------------------------""".trimIndent())
  }

  private fun logTest(version: Int, uuid: java.util.UUID, fromUUID: memeid.UUID, fromSigBits: memeid.UUID, fromString: memeid.UUID?) {
    println( """
      Generated Version $version UUID: $uuid | UUID Version: ${uuid.version()} | UUID Variant: ${uuid.version()} 
      |  From UUID: $fromUUID | version: ${fromUUID.version()} | variant: ${fromUUID.variant()}
      |  From significant bits: $fromSigBits | version: ${fromSigBits.version()} | variant: ${fromSigBits.variant()}
      |  From String: $fromString | version: ${fromString?.version()} | variant: ${fromString?.variant()}
      --------------------------------------------------------------------""".trimIndent())
  }
}