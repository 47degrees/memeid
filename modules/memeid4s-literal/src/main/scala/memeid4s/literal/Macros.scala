/*
 * Copyright 2019-2021 47 Degrees Open Source <https://www.47deg.com>
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

package memeid4s.literal

import scala.reflect.macros.blackbox

import memeid.UUID

private[literal] class Macros(val c: blackbox.Context) {
  import c.universe._

  def uuidInterpolator(args: Any*): c.Expr[UUID] = {
    if (args.nonEmpty)
      c.abort(c.enclosingPosition, "uuid interpolator should only be used on string literals")

    c.prefix.tree match {
      case interpolate(s) if isUUID(s) =>
        c.Expr[UUID](q"_root_.memeid.UUID.fromString($s)")
      case interpolate(s) => c.abort(c.enclosingPosition, s"invalid UUID: $s")
      case _              => c.abort(c.enclosingPosition, "should only be used on literals")
    }
  }

  private def isUUID(s: String): Boolean = _root_.scala.util.Try(UUID.fromString(s)).isSuccess

  private object interpolate {

    def unapply(t: c.Tree): Option[String] =
      t match {
        case Apply(_, List(Apply(_, List(Literal(Constant(s: String)))))) => Some(s)
        case _                                                            => None
      }

  }

}
