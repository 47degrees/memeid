/*
 * Copyright 2019-2020 47 Degrees Open Source <https://www.47deg.com>
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

import cats.kernel.laws.discipline.SerializableTests
import cats.laws.discipline.arbitrary._
import cats.laws.discipline.eq._
import cats.laws.discipline.{ContravariantTests, MiniInt}
import cats.{Contravariant, Eq}

import memeid4s.cats.instances._
import memeid4s.digest.Digestible
import org.scalacheck.Arbitrary
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline

class DigestibleLaws extends Specification with Discipline {

  implicit private def DigestibleArbitraryInstance: Arbitrary[Digestible[MiniInt]] =
    Arbitrary(Arbitrary.arbitrary[MiniInt => Array[Byte]].map(f => f(_)))

  implicit private val EqByteArrayInstance: Eq[Array[Byte]] = Eq.instance(_.toList === _.toList)

  implicit private def DigestibleEqInstance: Eq[Digestible[MiniInt]] =
    Eq.by[Digestible[MiniInt], MiniInt => Array[Byte]](_.toByteArray)

  checkAll("Digestible", ContravariantTests[Digestible].contravariant[MiniInt, MiniInt, MiniInt])
  checkAll("Digestible", SerializableTests.serializable(Contravariant[Digestible]))

}
