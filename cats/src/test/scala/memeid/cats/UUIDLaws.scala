package memeid.cats

import cats.instances.option._
import cats.kernel.laws.discipline.{HashTests, LowerBoundedTests, OrderTests, UpperBoundedTests}

import memeid.UUID
import memeid.cats.instances._
import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline

class UUIDLaws extends Specification with Discipline {

  checkAll("UUID", HashTests[UUID].hash)
  checkAll("UUID", OrderTests[UUID].order)
  checkAll("UUID", LowerBoundedTests[UUID].lowerBounded)
  checkAll("UUID", UpperBoundedTests[UUID].upperBounded)

}
