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
// res0: UUID = ddafe628-17f2-1171-a53d-9a72dd876511
UUID.V1.next
// res1: UUID = ddafea10-17f2-1171-a53d-9a72dd876511
UUID.V1.next
// res2: UUID = ddafedf8-17f2-1171-a53d-9a72dd876511
```

### Random (v4)

The cryptographically random variant, equivalent to `java.util.UUID/randomUUID`.

```scala
import memeid.UUID

UUID.V4.random
// res3: UUID = 209e390a-a8a5-4b43-a0cc-95556bf46aea
UUID.V4.random
// res4: UUID = 9e9de11d-20e0-4aaf-8d11-7f2776f3cd9e
UUID.V4.random
// res5: UUID = d7649591-b220-4d27-bc31-68ef54bf9ef7
```

### Namespaced (v3, v5)

Namespaced UUIDs are generated from a UUID (namespace) and a hashed value (name), V3 uses MD5 and V5 uses SHA1 hash.

```scala
import memeid.UUID

val namespace = UUID.V1.next
// namespace: UUID = ddaff9b0-17f2-1171-a53d-9a72dd876511

UUID.V3(namespace, "my-secret-code")
// res6: UUID = d205e08f-24bf-3af4-08af-e868932f06e9
UUID.V5(namespace, "my-secret-code")
// res7: UUID = 138ba6f9-3839-58fe-9b69-52b47194f9d0
```

If you want to hash a custom type, you must provide an implicit `memeid.digest.Digestible` instance. This instance is used to convert your type into a byte array for hashing.

```scala
import memeid.digest.Digestible

case class User(firstName: String, lastName: String)

implicit val digestibleUser: Digestible[User] =
  (u: User) => u.firstName.getBytes ++ u.lastName.getBytes
// digestibleUser: Digestible[User] = repl.Session$App$$anonfun$10@18476a56

UUID.V3(namespace, User("Federico", "García Lorca"))
// res8: UUID = 65a16c24-3f03-3152-2a26-2d59ab44c1a8
UUID.V5(namespace, User("Federico", "García Lorca"))
// res9: UUID = e66be2e7-8af8-51bb-d62c-1cf6989c3a56
```

### Semi-sequential, random (SQUUID)

SQUUIDs are a non-standard variaton of V4 UUIDs that are semi-sequential. They incorporate a time-component in their 32 most significant bits to generate UUIDs that don't fragment DB indexes.

```scala
import memeid.UUID

UUID.V4.squuid
// res10: UUID = 5e170f03-2842-411d-92c8-1ca9d2ba693e
UUID.V4.squuid
// res11: UUID = 5e170f03-1d24-4d98-a54f-62a2f1769793
UUID.V4.squuid
// res12: UUID = 5e170f03-5c88-4e95-80ad-3d17e7bd982e
```

## Java interoperability

`memeid` proviedes conversion method between `UUID` and `java.util.UUID`

```scala
import memeid.UUID
import memeid.JavaConverters._
import java.util.{ UUID => JavaUUID }

val j = JavaUUID.randomUUID
// j: java.util.UUID = 0c6cf6a4-d0c1-4cc5-a397-b6b02d961cdc
val u = UUID.V4.random
// u: UUID = 92297802-295e-408d-99af-e3df1a5c7a4e

j.asScala
// res13: UUID = 0c6cf6a4-d0c1-4cc5-a397-b6b02d961cdc
u.asJava
// res14: java.util.UUID = 92297802-295e-408d-99af-e3df1a5c7a4e
```

## Integrations

`memeid` provides several modules which integrate with popular third-party libraries. If you see something missing don't hesitate to open an issue or send a patch.

### Doobie

The [Doobie][https://github.com/tpolecat/doobie] integration allows you to use the `UUID` type mapped to your database's UUID type.

```scala
libraryDependencies += "com.47deg" % "memeid-doobie" % "0.1.0-SNAPSHOT"
```

To have the [UUID mappings][https://tpolecat.github.io/doobie/docs/12-Custom-Mappings.html] available in scope you can import `memeid.doobie.implicits`.

```scala
import cats.effect._

import doobie._
import doobie.implicits._
import doobie.h2.implicits._

import memeid.doobie.implicits._

import scala.concurrent.ExecutionContext

implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
// cs: ContextShift[IO] = cats.effect.internals.IOContextShift@252a2e85

val transactor: Transactor[IO] =
  Transactor.fromDriverManager[IO](
    driver = "org.h2.Driver",
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "",
    pass = ""
  )
// transactor: Transactor[IO] = doobie.util.transactor$Transactor$$anon$13@14a93eeb

// Create table
sql"CREATE TABLE IF NOT EXISTS test (id UUID NOT NULL)".update.run.transact(transactor).unsafeRunSync
// res15: Int = 0

def select(uuid: UUID): Query0[UUID] =
  sql"""SELECT id from test where id = ${uuid}""".query[UUID]

def insert(uuid: UUID): Update0 =
  sql"""insert into test (id) values ($uuid)""".update

val example = UUID.V1.next
// example: UUID = ddb03448-17f2-1171-a53d-9a72dd876511

val program: IO[UUID] = for {
  _ <- insert(example).run.transact(transactor)
  u <- select(example).unique.transact(transactor)
} yield u
// program: IO[UUID] = Bind(
//   Async(
//     cats.effect.internals.IOBracket$$$Lambda$7579/0x0000000840d44840@12c91db2,
//     false
//   ),
//   <function1>
// )

program.unsafeRunSync
// res16: UUID = ddb03448-17f2-1171-a53d-9a72dd876511
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
// uuid: UUID = ddb05770-17f2-1171-a53d-9a72dd876511
val json = Json.fromString(uuid.toString)
// json: Json = JString("ddb05770-17f2-1171-a53d-9a72dd876511")

Encoder[UUID].apply(uuid)
// res17: Json = JString("ddb05770-17f2-1171-a53d-9a72dd876511")
Decoder[UUID].decodeJson(json)
// res18: Decoder.Result[UUID] = Right(ddb05770-17f2-1171-a53d-9a72dd876511)
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
// res19: HttpRoutes[IO] = Kleisli(
//   org.http4s.HttpRoutes$$$Lambda$7352/0x0000000842263840@3be06329
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
// res20: HttpRoutes[IO] = Kleisli(
//   org.http4s.HttpRoutes$$$Lambda$7352/0x0000000842263840@74f0f718
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
// ns: UUID = ddb072c8-17f2-1171-a53d-9a72dd876511
UUID.random[IO].unsafeRunSync
// res21: UUID = 7275f23d-1b8d-4140-b2c3-19b837613978
UUID.v3[IO, String](ns, "my-secret-code").unsafeRunSync
// res22: UUID = d69ece53-8df5-367b-cb20-8581b568d324
UUID.v5[IO, String](ns, "my-secret-code").unsafeRunSync
// res23: UUID = 0e3a610b-97bb-59fb-73a1-2f1238b2011f
```

## References

- [RFC 4122 - A Universally Unique Identifier](https://www.ietf.org/rfc/rfc4122.txt)
- JVM [UUID type](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation
