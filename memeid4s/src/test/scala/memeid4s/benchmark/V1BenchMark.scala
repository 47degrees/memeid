package memeid.benchmark

import org.scalameter.{Bench, Gen}

object V1BenchMark extends Bench.LocalTime {
  val sizes: Gen[Int] = Gen.range("size")(300000, 1500000, 300000)

  val ranges: Gen[Range] = for {
    size <- sizes
  } yield 0 until size

  performance of "V1 creations" in {
    measure method "V1.next" in {
      using(ranges) in { r =>
        r.map(_ + 1)
      }
    }
  }
}
