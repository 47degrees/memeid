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
import memeid.scala.UUID

UUID.V1.next
// res0: UUID = d52aec98-18f4-1171-0acf-4f8704f64aad
UUID.V1.next
// res1: UUID = d52b0020-18f4-1171-0acf-4f8704f64aad
UUID.V1.next
// res2: UUID = d52b0bd8-18f4-1171-0acf-4f8704f64aad
```

### Random (v4)

The cryptographically random variant, equivalent to `java.util.UUID/randomUUID`.

```scala
UUID.V4.random
// res3: memeid.UUID.V4 = 94aa1d00-52d9-44f5-8478-2b754bdb41c3
UUID.V4.random
// res4: memeid.UUID.V4 = 73c2bf7e-d7f5-4680-bc31-560fd918c8bf
UUID.V4.random
// res5: memeid.UUID.V4 = 85f458b4-0854-455b-a127-43d04f956573
```

### Namespaced (v3, v5)

Namespaced UUIDs are generated from a UUID (namespace) and a hashed value (name), V3 uses MD5 and V5 uses SHA1 hash.

```scala
val namespace = UUID.V1.next
```

We can now create UUIDs with the namespace and an arbitrary value as the name. It automatically works with Strings and UUIDs:

```scala
UUID.V3(namespace, "my-secret-code")
// res6: memeid.UUID.V3 = c81f866d-aca6-3ffe-b4ea-3a4c30c3a1c3
UUID.V5(namespace, "my-secret-code")
// res7: memeid.UUID.V5 = 5cbc5c82-67de-5756-86a2-45e32b524c7f
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
// res8: memeid.UUID.V3 = c470704a-68c1-3c27-452c-098fabd96d72
UUID.V5(namespace, User("Federico", "García Lorca"))
// res9: memeid.UUID.V5 = 933d17f3-6c34-53b8-df2d-f3172d640d72
```

### Semi-sequential, random (SQUUID)

SQUUIDs are a non-standard variaton of V4 UUIDs that are semi-sequential. They incorporate a time-component in their 32 most significant bits to generate UUIDs that don't fragment DB indexes.

```scala
UUID.V4.squuid
// res10: UUID = 5e27f6f9-afe2-42bd-8e9d-0e96eec181b1
UUID.V4.squuid
// res11: UUID = 5e27f6f9-af2a-40a2-a903-74e3a3c6cf4a
UUID.V4.squuid
// res12: UUID = 5e27f6f9-9db6-442c-b3b9-1b5f8b31970a
```

## Java interoperability

`memeid` provides conversion method between `UUID` and `java.util.UUID` through:

```scala
val j = java.util.UUID.randomUUID
// j: java.util.UUID = c4652757-0aa8-4e6a-aa9a-2fa619842a5b
val u = UUID.V4.random
// u: memeid.UUID.V4 = fca13cb5-4b7b-4a4b-aa14-0d3338efd439

UUID.fromUUID(j)
// res13: UUID = c4652757-0aa8-4e6a-aa9a-2fa619842a5b
u.asJava
// res14: java.util.UUID = fca13cb5-4b7b-4a4b-aa14-0d3338efd439
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
// res18: UUID = d538f270-18f4-1171-0acf-4f8704f64aad
```

### Circe

```scala
libraryDependencies += "com.47deg" % "memeid-circe" % "0.1.0-SNAPSHOT"
```

You can import `memeid.circe.implicits` to have the `Encoder` and `Decoder` instances for `UUID` in scope.

```scala
import io.circe.{ Json, Encoder, Decoder }

import memeid.circe.implicits._

val uuid = UUID.V1.next
// uuid: UUID = d53aa7f0-18f4-1171-0acf-4f8704f64aad
val json = Json.fromString(uuid.toString)
// json: Json = JString("d53aa7f0-18f4-1171-0acf-4f8704f64aad")

Encoder[UUID].apply(uuid)
// res19: Json = JString("d53aa7f0-18f4-1171-0acf-4f8704f64aad")
Decoder[UUID].decodeJson(json)
// res20: Decoder.Result[UUID] = Right(d53aa7f0-18f4-1171-0acf-4f8704f64aad)
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
// res27: UUID = 3a99c186-8b3f-4acb-aafe-4f7f1ae8a58b
UUID.v3[IO, String](namespace, "my-secret-code").unsafeRunSync
// res28: UUID = c81f866d-aca6-3ffe-b4ea-3a4c30c3a1c3
UUID.v5[IO, String](namespace, "my-secret-code").unsafeRunSync
// res29: UUID = 5cbc5c82-67de-5756-86a2-45e32b524c7f
```

## References

- [RFC 4122 - A Universally Unique Identifier](https://www.ietf.org/rfc/rfc4122.txt)
- JVM [UUID type](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation
