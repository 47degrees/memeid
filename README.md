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
// res0: UUID = eaeca5b0-17f2-1171-a5ba-9a72dd876511
UUID.V1.next
// res1: UUID = eaecad80-17f2-1171-a5ba-9a72dd876511
UUID.V1.next
// res2: UUID = eaecb168-17f2-1171-a5ba-9a72dd876511
```

### Random (v4)

The cryptographically random variant, equivalent to `java.util.UUID/randomUUID`.

```scala
import memeid.UUID

UUID.V4.random
// res3: UUID = 14028299-aca4-46e3-96d8-ed439051116c
UUID.V4.random
// res4: UUID = 05cd951a-3b65-4545-a539-b8bd36ad2f13
UUID.V4.random
// res5: UUID = 757ad410-73a5-4771-936d-7d3712f63d3c
```

### Namespaced (v3, v5)

Namespaced UUIDs are generated from a UUID (namespace) and a hashed value (name), V3 uses MD5 and V5 uses SHA1 hash.

```scala
import memeid.UUID

val namespace = UUID.V1.next
// namespace: UUID = eaecc4f0-17f2-1171-a5ba-9a72dd876511

UUID.V3(namespace, "my-secret-code")
// res6: UUID = f1e30d54-6eee-39f3-4a6c-d43bb03a2116
UUID.V5(namespace, "my-secret-code")
// res7: UUID = 0200131a-bac1-5378-e4ad-3f41ddf13463
```

If you want to hash a custom type, you must provide an implicit `memeid.digest.Digestible` instance. This instance is used to convert your type into a byte array for hashing.

```scala
import memeid.digest.Digestible

case class User(firstName: String, lastName: String)

implicit val digestibleUser: Digestible[User] =
  (u: User) => u.firstName.getBytes ++ u.lastName.getBytes
// digestibleUser: Digestible[User] = repl.Session$App$$anonfun$10@3e0f1f2

UUID.V3(namespace, User("Federico", "García Lorca"))
// res8: UUID = 860d79bf-3e3a-3613-ec69-049f12f04be2
UUID.V5(namespace, User("Federico", "García Lorca"))
// res9: UUID = f95b8d7d-5aa6-56cc-8ae3-1deec3d8ddf7
```

### Semi-sequential, random (SQUUID)

SQUUIDs are a non-standard variaton of V4 UUIDs that are semi-sequential. They incorporate a time-component in their 32 most significant bits to generate UUIDs that don't fragment DB indexes.

```scala
import memeid.UUID

UUID.V4.squuid
// res10: UUID = 5e170fe1-28a4-4096-bc58-658e6e62a139
UUID.V4.squuid
// res11: UUID = 5e170fe1-5864-434a-ad48-3bb5ae10f019
UUID.V4.squuid
// res12: UUID = 5e170fe1-70c4-4cec-bd2e-3b194db571b2
```

## Java interoperability

`memeid` proviedes conversion method between `UUID` and `java.util.UUID`

```scala
import memeid.UUID
import memeid.JavaConverters._
import java.util.{ UUID => JavaUUID }

val j = JavaUUID.randomUUID
// j: java.util.UUID = 3ad57a87-251d-4735-a920-bf5885e1c272
val u = UUID.V4.random
// u: UUID = 92bf488c-ede3-4e9a-8712-390a9d57d09d

j.asScala
// res13: UUID = 3ad57a87-251d-4735-a920-bf5885e1c272
u.asJava
// res14: java.util.UUID = 92bf488c-ede3-4e9a-8712-390a9d57d09d
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
// cs: ContextShift[IO] = cats.effect.internals.IOContextShift@1ac3007

val transactor: Transactor[IO] =
  Transactor.fromDriverManager[IO](
    driver = "org.h2.Driver",
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "",
    pass = ""
  )
// transactor: Transactor[IO] = doobie.util.transactor$Transactor$$anon$13@5c946dab

// Create table
sql"CREATE TABLE IF NOT EXISTS test (id UUID NOT NULL)".update.run.transact(transactor).unsafeRunSync
// res15: Int = 0

def select(uuid: UUID): Query0[UUID] =
  sql"""SELECT id from test where id = ${uuid}""".query[UUID]

def insert(uuid: UUID): Update0 =
  sql"""insert into test (id) values ($uuid)""".update

val example = UUID.V1.next
// example: UUID = eafd5720-17f2-1171-a5ba-9a72dd876511

val program: IO[UUID] = for {
  _ <- insert(example).run.transact(transactor)
  u <- select(example).unique.transact(transactor)
} yield u
// program: IO[UUID] = Bind(
//   Async(
//     cats.effect.internals.IOBracket$$$Lambda$8266/0x0000000842314840@3ad3701a,
//     false
//   ),
//   <function1>
// )

program.unsafeRunSync
// res16: UUID = eafd5720-17f2-1171-a5ba-9a72dd876511
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
// uuid: UUID = eafeb2c8-17f2-1171-a5ba-9a72dd876511
val json = Json.fromString(uuid.toString)
// json: Json = JString("eafeb2c8-17f2-1171-a5ba-9a72dd876511")

Encoder[UUID].apply(uuid)
// res17: Json = JString("eafeb2c8-17f2-1171-a5ba-9a72dd876511")
Decoder[UUID].decodeJson(json)
// res18: Decoder.Result[UUID] = Right(eafeb2c8-17f2-1171-a5ba-9a72dd876511)
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
//   org.http4s.HttpRoutes$$$Lambda$8399/0x00000008423fd040@42aa97cc
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
//   org.http4s.HttpRoutes$$$Lambda$8399/0x00000008423fd040@4ce22452
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
// ns: UUID = eaffb880-17f2-1171-a5ba-9a72dd876511
UUID.random[IO].unsafeRunSync
// res21: UUID = 1b4424eb-5368-4833-9732-a1c94cf489f0
UUID.v3[IO, String](ns, "my-secret-code").unsafeRunSync
// res22: UUID = 799291d9-e8ba-35ec-246b-049a3b4d456e
UUID.v5[IO, String](ns, "my-secret-code").unsafeRunSync
// res23: UUID = 094f0ce6-1cce-5a90-872c-b69d3d7ea589
```

## References

- [RFC 4122 - A Universally Unique Identifier](https://www.ietf.org/rfc/rfc4122.txt)
- JVM [UUID type](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation
