package memeid.cats

import memeid._
import memeid.arbitraries._
import memeid.cats.implicits._

import _root_.cats.instances.option._
import _root_.cats.kernel.laws.discipline.{HashTests, LowerBoundedTests, OrderTests, UpperBoundedTests}

import org.specs2.mutable.Specification
import org.typelevel.discipline.specs2.mutable.Discipline

class UUIDLaws extends Specification with Discipline {

  checkAll("UUID", HashTests[UUID].hash)
  checkAll("UUID", OrderTests[UUID].order)
  checkAll("UUID", LowerBoundedTests[UUID].lowerBounded)
  checkAll("UUID", UpperBoundedTests[UUID].upperBounded)

}
