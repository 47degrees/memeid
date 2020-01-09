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
// res0: UUID = f19151f0-17f1-1171-a542-9a72dd876511
UUID.V1.next
// res1: UUID = f1915da8-17f1-1171-a542-9a72dd876511
UUID.V1.next
// res2: UUID = f1916190-17f1-1171-a542-9a72dd876511
```

### Random (v4)

The cryptographically random variant, equivalent to `java.util.UUID/randomUUID`.

```scala
import memeid.UUID

UUID.V4.random
// res3: UUID = 84c39ebd-9530-4625-943f-8f553998b30e
UUID.V4.random
// res4: UUID = c8bf3e82-7ddd-4582-9797-213a0ba62aaf
UUID.V4.random
// res5: UUID = 173474f9-f0c7-4ea7-80c8-60857991b3a8
```

### Namespaced (v3, v5)

Namespaced UUIDs are generated from a UUID (namespace) and a hashed value (name), V3 uses MD5 and V5 uses SHA1 hash.

```scala
import memeid.UUID

val namespace = UUID.V1.next
// namespace: UUID = f1917130-17f1-1171-a542-9a72dd876511

UUID.V3(namespace, "my-secret-code")
// res6: UUID = fdc2d828-eb95-3f63-646f-2450103d1c89
UUID.V5(namespace, "my-secret-code")
// res7: UUID = c0017d80-8e8d-5179-c563-a08e4bce1a4e
```

// TODO: custom digestible instances

### Semi-sequential, random (SQUUID)

SQUUIDs are a non-standard variaton of V4 UUIDs that are semi-sequential. They incorporate a time-component in their 32 most significant bits to generate UUIDs that don't fragment DB indexes.

```scala
import memeid.UUID

UUID.V4.squuid
// res8: UUID = 5e16ff89-e160-49c9-a236-8bc9e82290ae
UUID.V4.squuid
// res9: UUID = 5e16ff89-360a-4053-bc6a-128c9248e171
UUID.V4.squuid
// res10: UUID = 5e16ff89-1458-423b-bd65-6d198c3a19b8
```

## Java interoperability

`memeid` proviedes conversion method between `UUID` and `java.util.UUID`

```scala
import memeid.UUID
import memeid.JavaConverters._
import java.util.{ UUID => JavaUUID }

val j = JavaUUID.randomUUID
// j: java.util.UUID = b0914be2-2faa-494a-9ce7-d76abe63990e
val u = UUID.V4.random
// u: UUID = 1a322a3a-8914-47a7-8408-4179794809ef

j.asScala
// res11: UUID = b0914be2-2faa-494a-9ce7-d76abe63990e
u.asJava
// res12: java.util.UUID = 1a322a3a-8914-47a7-8408-4179794809ef
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
import memeid.doobie.implicits._
// TODO: h2 example
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
// uuid: UUID = f191a010-17f1-1171-a542-9a72dd876511
val json = Json.fromString(uuid.toString)
// json: Json = JString("f191a010-17f1-1171-a542-9a72dd876511")

Encoder[UUID].apply(uuid)
// res13: Json = JString("f191a010-17f1-1171-a542-9a72dd876511")
Decoder[UUID].decodeJson(json)
// res14: Decoder.Result[UUID] = Right(f191a010-17f1-1171-a542-9a72dd876511)
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
// res15: HttpRoutes[IO] = Kleisli(
//   org.http4s.HttpRoutes$$$Lambda$5469/0x0000000841a2d040@34f3d786
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
// res16: HttpRoutes[IO] = Kleisli(
//   org.http4s.HttpRoutes$$$Lambda$5469/0x0000000841a2d040@36e1d98d
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
// ns: UUID = f1935d60-17f1-1171-a542-9a72dd876511
UUID.random[IO].unsafeRunSync
// res17: UUID = 0b2a5ba7-57d8-44dc-8a22-82864af084f9
UUID.v3[IO, String](ns, "my-secret-code").unsafeRunSync
// res18: UUID = 59cc8d78-de89-3b0f-49a3-a56947f3ed0d
UUID.v5[IO, String](ns, "my-secret-code").unsafeRunSync
// res19: UUID = 82e6c816-fe97-5f48-d92c-1b3ee1514bd2
```

## References

- [RFC 4122 - A Universally Unique Identifier](https://www.ietf.org/rfc/rfc4122.txt)
- JVM [UUID type](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation
