/*
 * Copyright 2019-2023 47 Degrees Open Source <https://www.47deg.com>
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

import scala.quoted.*

import memeid.UUID

object Macros {

  given UUIDToExpr: ToExpr[UUID] with {

    @SuppressWarnings(Array("scalafix:Disable.toString"))
    def apply(x: UUID)(using Quotes): Expr[UUID] =
      '{ UUID.fromString(${ Expr(x.toString) }) }

  }

  def uuidInterpolator(sops: Expr[UUIDContextOps])(using quotes: Quotes): Expr[UUID] = {
    import quotes.reflect.*

    val values: List[String] = sops.asTerm.underlyingArgument match {
      case Apply(conv, List(Apply(fun, List(Typed(Repeated(values, _), _))))) =>
        values.collect { case Literal(StringConstant(value)) => value }
    }

    def errorMsg(s: String): String =
      s"error: invalid UUID: $s".stripMargin

    values match
      case head :: Nil =>
        scala.util
          .Try(UUID.fromString(head))
          .toEither
          .fold(_ => report.errorAndAbort(errorMsg(head)), uuid => Expr[UUID](uuid))
      case _ => report.errorAndAbort("uuid interpolator should only be used on string literals")
  }

}
