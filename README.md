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
// res0: UUID = 07e1f828-186b-1171-811c-34115d4b01c7
UUID.V1.next
// res1: UUID = 07e203e0-186b-1171-811c-34115d4b01c7
UUID.V1.next
// res2: UUID = 07e207c8-186b-1171-811c-34115d4b01c7
```

### Random (v4)

The cryptographically random variant, equivalent to `java.util.UUID/randomUUID`.

```scala
import memeid.UUID

UUID.V4.random
// res3: UUID = b3d68c46-a88b-4b26-a95b-19a73a2330a2
UUID.V4.random
// res4: UUID = 3a9f2568-437a-4768-a8be-6996bfac416e
UUID.V4.random
// res5: UUID = c4bd29f5-836c-486b-8194-f4c230bbf68a
```

### Namespaced (v3, v5)

Namespaced UUIDs are generated from a UUID (namespace) and a hashed value (name), V3 uses MD5 and V5 uses SHA1 hash.

```scala
import memeid.UUID

val namespace = UUID.V1.next
```

We can now create UUIDs with the namespace and an arbitrary value as the name. It automatically works with Strings and UUIDs:

```scala
UUID.V3(namespace, "my-secret-code")
// res6: UUID = 18b33d47-de7e-355d-3ce2-f9c1d3613027
UUID.V5(namespace, "my-secret-code")
// res7: UUID = 215dbb4b-0cd6-5bb4-0ee5-7f8260febc07
```

If you want to hash a custom type, you must provide an implicit `memeid.digest.Digestible` instance.

```scala
import memeid.digest.Digestible

case class User(firstName: String, lastName: String)

implicit val digestibleUser: Digestible[User] =
  (u: User) => u.firstName.getBytes ++ u.lastName.getBytes
```

The implicit instance is used to convert your type into a byte array for hashing:

```scala
UUID.V3(namespace, User("Federico", "García Lorca"))
// res8: UUID = 5746c0bd-b70d-3bc2-95ea-d1eab78e2492
UUID.V5(namespace, User("Federico", "García Lorca"))
// res9: UUID = 41714d97-8db4-5560-7e20-9c41ae85b71a
```

### Semi-sequential, random (SQUUID)

SQUUIDs are a non-standard variaton of V4 UUIDs that are semi-sequential. They incorporate a time-component in their 32 most significant bits to generate UUIDs that don't fragment DB indexes.

```scala
import memeid.UUID

UUID.V4.squuid
// res10: UUID = 5e1eef0b-8a71-4183-b6ff-9cbdbb2266ca
UUID.V4.squuid
// res11: UUID = 5e1eef0b-8157-49a3-9d88-9ceed9b6f0b2
UUID.V4.squuid
// res12: UUID = 5e1eef0b-dbda-4572-bcf1-479856523542
```

## Java interoperability

`memeid` proviedes conversion method between `UUID` and `java.util.UUID` through `memeid.JavaConverters`:

```scala
import memeid.JavaConverters._


val j = java.util.UUID.randomUUID
// j: java.util.UUID = 5ac741e7-7390-4025-baec-c8e9cdb50df0
val u = UUID.V4.random
// u: UUID = 618ea533-a540-4e05-a3df-b760421c8f93

j.asScala
// res13: UUID = 5ac741e7-7390-4025-baec-c8e9cdb50df0
u.asJava
// res14: java.util.UUID = 618ea533-a540-4e05-a3df-b760421c8f93
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
import memeid.doobie.implicits._

def select(uuid: UUID): Query0[UUID] =
  sql"""SELECT id from test where id = ${uuid}""".query[UUID]

def insert(uuid: UUID): Update0 =
  sql"""insert into test (id) values ($uuid)""".update

val example = UUID.V1.next
```

```scala
{
  for {
    _ <- insert(example).run.transact(transactor)
    u <- select(example).unique.transact(transactor)
  } yield u
}.unsafeRunSync
// res18: UUID = 07efee60-186b-1171-811c-34115d4b01c7
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
// uuid: UUID = 07f13298-186b-1171-811c-34115d4b01c7
val json = Json.fromString(uuid.toString)
// json: Json = JString("07f13298-186b-1171-811c-34115d4b01c7")

Encoder[UUID].apply(uuid)
// res19: Json = JString("07f13298-186b-1171-811c-34115d4b01c7")
Decoder[UUID].decodeJson(json)
// res20: Decoder.Result[UUID] = Right(07f13298-186b-1171-811c-34115d4b01c7)
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
```

## Cats & Cats-effect

```scala
libraryDependencies += "com.47deg" % "memeid-cats" % "0.1.0-SNAPSHOT"
```

The cats integration provides typeclass implementation for `UUID`, as well as effectful constructors for UUIDs for integration with programs that use `cats-effect`.

### Typeclasses

```scala
import cats._
import memeid.cats.implicits._

Order[UUID]
Hash[UUID]
Eq[UUID]
Show[UUID]
```

### Constructors

```scala
import memeid.cats.implicits._

UUID.random[IO].unsafeRunSync
// res27: UUID = 1b1c4142-4c2d-45e6-ad72-8c6839d2cf2d
UUID.v3[IO, String](namespace, "my-secret-code").unsafeRunSync
// res28: UUID = 18b33d47-de7e-355d-3ce2-f9c1d3613027
UUID.v5[IO, String](namespace, "my-secret-code").unsafeRunSync
// res29: UUID = 215dbb4b-0cd6-5bb4-0ee5-7f8260febc07
```

## References

- [RFC 4122 - A Universally Unique Identifier](https://www.ietf.org/rfc/rfc4122.txt)
- JVM [UUID type](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation
