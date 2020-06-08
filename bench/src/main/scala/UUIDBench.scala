package memeid4s.bench

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

import memeid4s.UUID
import org.openjdk.jmh.annotations._

object UUIDStates {
  val namespace = UUID.V1.next

  @SuppressWarnings(Array("scalafix:Disable.toString"))
  val uuids: Array[String] = (1 to 100).map(_ => UUID.V1.next.toString).toArray

  @State(Scope.Benchmark)
  class RNG {
    def uuid: String = uuids(ThreadLocalRandom.current().nextInt(uuids.length))
  }

}

@Warmup(
  iterations = 5,
  time = 1,
  timeUnit = TimeUnit.SECONDS
)
@Measurement(
  iterations = 5,
  time = 1,
  timeUnit = TimeUnit.SECONDS
)
class UUIDBenchmark {

  import UUIDStates._

  @Benchmark
  def timeBased(): UUID =
    UUID.V1.next

  @Benchmark
  def random(): UUID =
    UUID.V4.random

  @Benchmark
  def squuid(): UUID =
    UUID.V4.squuid

  @Benchmark
  def namespacedV3(x: RNG): UUID =
    UUID.V3(namespace, x.uuid)

  @Benchmark
  def namespacedV5(x: RNG): UUID =
    UUID.V5(namespace, x.uuid)

  @Benchmark
  def fromString(x: RNG): UUID =
    memeid.UUID.fromString(x.uuid)
}
