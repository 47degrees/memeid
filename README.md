# memeid

`memeid` is a Scala library for generating [RFC-compliant](https://www.ietf.org/rfc/rfc4122.txt) Universal Unique Identifiers (UUIDs).

> A universally unique identifier (UUID) is a 128-bit number used to identify information in computer systems.
>
> When generated according to the standard methods, UUIDs are for practical purposes unique. Their uniqueness does not depend on a central registration authority or coordination between the parties generating them, unlike most other numbering schemes.
-- [Wikipedia article on UUIDs](https://en.wikipedia.org/wiki/Universally_unique_identifier)

## Rationale

The UUID type that ships with the JVM `java.util.UUID` has a number of problems, namely:

 - A bug in the comparison function that will never be fixed https://bugs.java.com/bugdatabase/view_bug.do?bug_id=7025832
 - Only provides UUID generation for random (V4) and non-namespaced pseudo-V3 UUIDs

This library aims to solve the aforementioned issues and provide an RFC-compliant `UUID` type with coherent comparison, a rich API to get its different fields and constructors for the UUID variants.

## Install

Add this to your `build.sbt` file:

```scala
libraryDependencies += "com.47deg" % "memeid" % "0.1.0-SNAPSHOT"
```

## UUID construction

### Time-based (v1)

The time-based (V1) variant of UUIDs is the fastest to generate. It uses a monotonic clock and node information to generate UUIDs.

```scala
import memeid.UUID

UUID.V1.next
// res0: UUID = d259be80-1805-1171-86d3-fa0787afc6eb
UUID.V1.next
// res1: UUID = d259ca38-1805-1171-86d3-fa0787afc6eb
UUID.V1.next
// res2: UUID = d259d208-1805-1171-86d3-fa0787afc6eb
```

### Random (v4)

The cryptographically random variant, equivalent to `java.util.UUID/randomUUID`.

```scala
import memeid.UUID

UUID.V4.random
// res3: UUID = 4aea3286-e38d-409f-8a7b-b78e2b45ac59
UUID.V4.random
// res4: UUID = b7c14cc5-337c-42ce-a7bc-2aa389c06c0a
UUID.V4.random
// res5: UUID = 9fbeaf66-f058-4404-9233-3338fc07f55e
```

### Namespaced (v3, v5)

Namespaced UUIDs are generated from a UUID (namespace) and a hashed value (name), V3 uses MD5 and V5 uses SHA1 hash.

```scala
import memeid.UUID

val namespace = UUID.V1.next
// namespace: UUID = d259e590-1805-1171-86d3-fa0787afc6eb

UUID.V3(namespace, "my-secret-code")
// res6: UUID = 9702c5ec-943c-375a-dbe5-7096afb44f89
UUID.V5(namespace, "my-secret-code")
// res7: UUID = cedf5542-4d1e-5b76-6e66-e7f43426c544
```

If you want to hash a custom type, you must provide an implicit `memeid.digest.Digestible` instance. This instance is used to convert your type into a byte array for hashing.

```scala
import memeid.digest.Digestible

case class User(firstName: String, lastName: String)

implicit val digestibleUser: Digestible[User] =
  (u: User) => u.firstName.getBytes ++ u.lastName.getBytes
// digestibleUser: Digestible[User] = repl.Session$App$$anonfun$10@3923b580

UUID.V3(namespace, User("Federico", "García Lorca"))
// res8: UUID = c08a2c0b-8555-3163-2463-16a8a488d102
UUID.V5(namespace, User("Federico", "García Lorca"))
// res9: UUID = 2367ac01-0869-5385-ba61-545a77dd4e42
```

### Semi-sequential, random (SQUUID)

SQUUIDs are a non-standard variaton of V4 UUIDs that are semi-sequential. They incorporate a time-component in their 32 most significant bits to generate UUIDs that don't fragment DB indexes.

```scala
import memeid.UUID

UUID.V4.squuid
// res10: UUID = 5e184d09-88d2-426f-9f50-101653850b69
UUID.V4.squuid
// res11: UUID = 5e184d09-bdbb-4a5a-85b3-af51b00e89a0
UUID.V4.squuid
// res12: UUID = 5e184d09-f4a5-4998-b8e8-fcd50315e8af
```

## Java interoperability

`memeid` proviedes conversion method between `UUID` and `java.util.UUID`

```scala
import memeid.UUID
import memeid.JavaConverters._
import java.util.{ UUID => JavaUUID }

val j = JavaUUID.randomUUID
// j: java.util.UUID = 8759f606-fec9-46bd-8474-c28060c54c7a
val u = UUID.V4.random
// u: UUID = 7b9e7900-68f7-408e-964e-9cbe8889623a

j.asScala
// res13: UUID = 8759f606-fec9-46bd-8474-c28060c54c7a
u.asJava
// res14: java.util.UUID = 7b9e7900-68f7-408e-964e-9cbe8889623a
```

## Literal syntax

`memeid` provides literal syntax with compile-time verification for UUIDs with the `uuid` interpolator. To use it, add this to your `build.sbt`:


```scala
libraryDependencies += "com.47deg" % "memeid-literal" % "0.1.0-SNAPSHOT"
```

We can now create UUIDs with literal syntax by importing `memeid.literal._`

```scala
import memeid.literal._

uuid"cb096727-6a82-4abd-bc79-fc92be8c5d88"
// res15: UUID = cb096727-6a82-4abd-bc79-fc92be8c5d88
```

Invalid UUID literals will fail at compile time:

```scala
import memeid.literal._

uuid"not-a-uuid"
// error: invalid UUID: not-a-uuid
// uuid"not-a-uuid"
// ^^^^^^^^^^^^^^^^
```

## Integrations

`memeid` provides several modules which integrate with popular third-party libraries. If you see something missing don't hesitate to open an issue or send a patch.

### Doobie

The [Doobie](https://github.com/tpolecat/doobie) integration allows you to use the `UUID` type mapped to your database's UUID type.

```scala
libraryDependencies += "com.47deg" % "memeid-doobie" % "0.1.0-SNAPSHOT"
```

To have the [UUID mappings](https://tpolecat.github.io/doobie/docs/12-Custom-Mappings.html) available in scope you can import `memeid.doobie.implicits`.

```scala
import cats.effect._

import doobie._
import doobie.implicits._
import doobie.h2.implicits._

import memeid.doobie.implicits._

import scala.concurrent.ExecutionContext

implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
// cs: ContextShift[IO] = cats.effect.internals.IOContextShift@7663a0de

val transactor: Transactor[IO] =
  Transactor.fromDriverManager[IO](
    driver = "org.h2.Driver",
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "",
    pass = ""
  )
// transactor: Transactor[IO] = doobie.util.transactor$Transactor$$anon$13@3946e595

// Create table
sql"CREATE TABLE IF NOT EXISTS test (id UUID NOT NULL)".update.run.transact(transactor).unsafeRunSync
// res17: Int = 0

def select(uuid: UUID): Query0[UUID] =
  sql"""SELECT id from test where id = ${uuid}""".query[UUID]

def insert(uuid: UUID): Update0 =
  sql"""insert into test (id) values ($uuid)""".update

val example = UUID.V1.next
// example: UUID = d26cc980-1805-1171-86d3-fa0787afc6eb

val program: IO[UUID] = for {
  _ <- insert(example).run.transact(transactor)
  u <- select(example).unique.transact(transactor)
} yield u
// program: IO[UUID] = Bind(
//   Async(
//     cats.effect.internals.IOBracket$$$Lambda$7538/0x00000008421ac040@13f97626,
//     false
//   ),
//   <function1>
// )

program.unsafeRunSync
// res18: UUID = d26cc980-1805-1171-86d3-fa0787afc6eb
```

### Circe

```scala
libraryDependencies += "com.47deg" % "memeid-circe" % "0.1.0-SNAPSHOT"
```

You can import `memeid.circe.implicits` to have the `Encoder` and `Decoder` instances for `UUID` in scope.

```scala
import io.circe.{ Json, Encoder, Decoder }

import memeid.UUID
import memeid.circe.implicits._

val uuid = UUID.V1.next
// uuid: UUID = d26effe8-1805-1171-86d3-fa0787afc6eb
val json = Json.fromString(uuid.toString)
// json: Json = JString("d26effe8-1805-1171-86d3-fa0787afc6eb")

Encoder[UUID].apply(uuid)
// res19: Json = JString("d26effe8-1805-1171-86d3-fa0787afc6eb")
Decoder[UUID].decodeJson(json)
// res20: Decoder.Result[UUID] = Right(d26effe8-1805-1171-86d3-fa0787afc6eb)
```

### Http4s

```scala
libraryDependencies += "com.47deg" % "memeid-http4s" % "0.1.0-SNAPSHOT"
```

### Path parameters

Using `UUID` companion object we can extract UUIDs from path parameters in URLs:

```scala
import cats.effect._

import org.http4s._
import org.http4s.dsl.io._

import memeid.UUID

HttpRoutes.of[IO] {
  case GET -> Root / "user" / UUID(uuid) => Ok(s"Hello, ${uuid}!")
}
// res21: HttpRoutes[IO] = Kleisli(
//   org.http4s.HttpRoutes$$$Lambda$7671/0x0000000842296840@3c55a041
// )
```

### Query parameters

The http4s integrations provides implicit instances for `QueryParamDecoder[UUID]` and `QueryParamEncoder[UUID]`, which you can use to derive matchers for query parameters or send UUID in request query parameters.

```scala
import cats.effect._

import org.http4s._
import org.http4s.dsl.io._

import memeid.UUID
import memeid.http4s.implicits._

object UUIDParamDecoder extends QueryParamDecoderMatcher[UUID]("uuid")

HttpRoutes.of[IO] {
  case GET -> Root / "user" :? UUIDParamDecoder(uuid) => Ok(s"Hello, ${uuid}!")
}
// res22: HttpRoutes[IO] = Kleisli(
//   org.http4s.HttpRoutes$$$Lambda$7671/0x0000000842296840@5910e692
// )
```

## Cats & Cats-effect

```scala
libraryDependencies += "com.47deg" % "memeid-cats" % "0.1.0-SNAPSHOT"
```

The cats integration provides typeclass implementation for `UUID`, as well as effectful constructors for UUIDs for integration with programs that use `cats-effect`.

```scala
import memeid.cats.implicits._

val ns = UUID.v1[IO].unsafeRunSync
// ns: UUID = d2703c50-1805-1171-86d3-fa0787afc6eb
UUID.random[IO].unsafeRunSync
// res23: UUID = 58599345-d181-4c05-b4e2-3e0ce4e4ad2b
UUID.v3[IO, String](ns, "my-secret-code").unsafeRunSync
// res24: UUID = b8da7649-7620-30fe-712e-2c539b00699f
UUID.v5[IO, String](ns, "my-secret-code").unsafeRunSync
// res25: UUID = f753e7d1-2b6b-5882-9caf-1e3bf0813860
```

## References

- [RFC 4122 - A Universally Unique Identifier](https://www.ietf.org/rfc/rfc4122.txt)
- JVM [UUID type](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation
