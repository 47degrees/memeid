package memeid

package object literal {

  @SuppressWarnings(Array("scalafix:Disable.Any"))
  implicit class UUIDInterpolator(private val sc: StringContext) extends AnyVal {

    /** Validates and transforms a literal string as a valid UUID in compile time */
    def uuid(args: Any*): UUID = macro Macros.uuidInterpolator

  }

}
