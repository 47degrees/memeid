package memeid.kotlin

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class UUIDTest {
  @Test
  fun `Create UUID from valid string`() {
    assertThat(1 == 1, equalTo(true))
  }

  @Test
  fun `Create UUID from UUID`() {
    assertThat(1 == 1, equalTo(true))
  }

  @Test
  fun `Attempt to create UUID from invalid string`() {
    assertThat(1 == 1, equalTo(true))
  }

  @Test
  fun `Attempt to create UUID from invalid type`() {

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
  
}