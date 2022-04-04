/*
 * Copyright 2019-2022 47 Degrees Open Source <https://www.47deg.com>
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

import cats.instances.option._
import cats.kernel.laws.discipline.HashTests
import cats.kernel.laws.discipline.LowerBoundedTests
import cats.kernel.laws.discipline.OrderTests
import cats.kernel.laws.discipline.UpperBoundedTests

import memeid4s.UUID
import memeid4s.cats.instances._
import memeid4s.scalacheck.arbitrary.instances._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline

class UUIDLaws extends Specification with Discipline {

  implicit private val UUID2UUIDArbitraryInstance: Arbitrary[UUID => UUID] = Arbitrary(
    arbitrary[UUID].map(uuid => (_: UUID) => uuid)
  )

  checkAll("UUID", HashTests[UUID].hash)
  checkAll("UUID", OrderTests[UUID].order)
  checkAll("UUID", LowerBoundedTests[UUID].lowerBounded)
  checkAll("UUID", UpperBoundedTests[UUID].upperBounded)

}
