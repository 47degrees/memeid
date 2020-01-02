package memeid.literal

import scala.reflect.macros.blackbox

import memeid.UUID

private[literal] class Macros(val c: blackbox.Context) {
  import c.universe._

  @SuppressWarnings(Array("scalafix:Disable.Any"))
  def uuidInterpolator(args: Any*): c.Expr[UUID] = {
    if (args.nonEmpty)
      c.abort(c.enclosingPosition, "uuid interpolator should only be used on string literals")

    c.prefix.tree match {
      case interpolate(s) if isUUID(s) => c.Expr[UUID](q"_root_.memeid.UUID.from($s).right.get")
      case interpolate(s)              => c.abort(c.enclosingPosition, s"invalid UUID: $s")
      case _                           => c.abort(c.enclosingPosition, "should only be used on literals")
    }
  }

  private def isUUID(s: String): Boolean = UUID.from(s).isRight

  private object interpolate {

    def unapply(t: c.Tree): Option[String] = t match {
      case Apply(_, List(Apply(_, List(Literal(Constant(s: String)))))) => Some(s)
      case _                                                            => None
    }

  }

}
