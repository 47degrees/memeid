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

package memeid4s.cats

import java.lang.Long.compareUnsigned

import cats.Contravariant
import cats.Show
import cats.kernel._

import memeid4s.UUID
import memeid4s.UUID.RichUUID
import memeid4s.digest.Digestible

trait instances {

  implicit def DigestibleContravariant: Contravariant[Digestible] =
    new Contravariant[Digestible] {

      override def contramap[A, B](fa: Digestible[A])(f: B => A): Digestible[B] =
        b => fa.toByteArray(f(b))

    }

  implicit def UUIDHashOrderInstances: Order[UUID] with Hash[UUID] =
    new Order[UUID] with Hash[UUID] {

      override def hash(x: UUID): Int = x.hashCode() /* scalafix:ok */

      override def compare(x: UUID, y: UUID): Int =
        compareUnsigned(x.msb, y.msb) match {
          case 0 => compareUnsigned(x.lsb, y.lsb)
          case x => x
        }

    }

  implicit def UUIDShowInstance: Show[UUID] = Show.fromToString[UUID]

  implicit def UUIDBoundsInstances: LowerBounded[UUID] with UpperBounded[UUID] =
    new LowerBounded[UUID] with UpperBounded[UUID] {

      override def minBound: UUID = UUID.Nil

      override def maxBound: UUID = UUID.from(-1L, -1L)

      override def partialOrder: PartialOrder[UUID] = UUIDHashOrderInstances
    }

}

object instances extends instances
