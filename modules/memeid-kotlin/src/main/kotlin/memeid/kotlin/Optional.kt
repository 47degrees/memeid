package memeid.kotlin

sealed class Optional<T>(private val value: T?) {

  abstract val isEmpty: Boolean

  fun get(): T {
    if (value == null) throw NoSuchElementException("No value present")
    return value
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false

    val optional1 = other as Optional<*>?

    return if (value != null) value == optional1!!.value else optional1!!.value == null
  }

  override fun hashCode(): Int = value?.hashCode() ?: 0
}

data class Some<T>(val value: T?): Optional<T>(value) {
  override val isEmpty: Boolean
    get() = true
}

class None<T>: Optional<T>(null) {
  override val isEmpty: Boolean
    get() = false
}