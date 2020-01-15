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
libraryDependencies += "com.47deg" % "memeid" % "@VERSION@"
```

## UUID construction

### Time-based (v1)

The time-based (V1) variant of UUIDs is the fastest to generate. It uses a monotonic clock and node information to generate UUIDs.

```scala mdoc
import memeid.UUID

UUID.V1.next
UUID.V1.next
UUID.V1.next
```

### Random (v4)

The cryptographically random variant, equivalent to `java.util.UUID/randomUUID`.

```scala mdoc
import memeid.UUID

UUID.V4.random
UUID.V4.random
UUID.V4.random
```

### Namespaced (v3, v5)

Namespaced UUIDs are generated from a UUID (namespace) and a hashed value (name), V3 uses MD5 and V5 uses SHA1 hash.

```scala mdoc:silent
import memeid.UUID

val namespace = UUID.V1.next
```

We can now create UUIDs with the namespace and an arbitrary value as the name. It automatically works with Strings and UUIDs:

```scala mdoc
UUID.V3(namespace, "my-secret-code")
UUID.V5(namespace, "my-secret-code")
```

If you want to hash a custom type, you must provide an implicit `memeid.digest.Digestible` instance. This instance is used to convert your type into a byte array for hashing.

```scala mdoc
import memeid.digest.Digestible

case class User(firstName: String, lastName: String)

implicit val digestibleUser: Digestible[User] =
  (u: User) => u.firstName.getBytes ++ u.lastName.getBytes

UUID.V3(namespace, User("Federico", "García Lorca"))
UUID.V5(namespace, User("Federico", "García Lorca"))
```

### Semi-sequential, random (SQUUID)

SQUUIDs are a non-standard variaton of V4 UUIDs that are semi-sequential. They incorporate a time-component in their 32 most significant bits to generate UUIDs that don't fragment DB indexes.

```scala mdoc
import memeid.UUID

UUID.V4.squuid
UUID.V4.squuid
UUID.V4.squuid
```

## Java interoperability

`memeid` proviedes conversion method between `UUID` and `java.util.UUID` through `memeid.JavaConverters`:

```scala mdoc
import memeid.JavaConverters._


val j = java.util.UUID.randomUUID
val u = UUID.V4.random

j.asScala
u.asJava
```

## Literal syntax

`memeid` provides literal syntax with compile-time verification for UUIDs with the `uuid` interpolator. To use it, add this to your `build.sbt`:


```scala
libraryDependencies += "com.47deg" % "memeid-literal" % "@VERSION@"
```

We can now create UUIDs with literal syntax by importing `memeid.literal._`

```scala mdoc
import memeid.literal._

uuid"cb096727-6a82-4abd-bc79-fc92be8c5d88"
```

Invalid UUID literals will fail at compile time:

```scala mdoc:fail
import memeid.literal._

uuid"not-a-uuid"
```

## Integrations

`memeid` provides several modules which integrate with popular third-party libraries. If you see something missing don't hesitate to open an issue or send a patch.

### Doobie

The [Doobie](https://github.com/tpolecat/doobie) integration allows you to use the `UUID` type mapped to your database's UUID type.

```scala
libraryDependencies += "com.47deg" % "memeid-doobie" % "@VERSION@"
```

To have the [UUID mappings](https://tpolecat.github.io/doobie/docs/12-Custom-Mappings.html) available in scope you can import `memeid.doobie.implicits`.

```scala mdoc:invisible
import cats.effect._

import doobie._
import doobie.h2.implicits._
import doobie.implicits._

import scala.concurrent.ExecutionContext

implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

val transactor: Transactor[IO] =
  Transactor.fromDriverManager[IO](
    driver = "org.h2.Driver",
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "",
    pass = ""
  )

sql"CREATE TABLE IF NOT EXISTS test (id UUID NOT NULL)".update.run.transact(transactor).unsafeRunSync
```

```scala mdoc:silent
import memeid.doobie.implicits._

def select(uuid: UUID): Query0[UUID] =
  sql"""SELECT id from test where id = ${uuid}""".query[UUID]

def insert(uuid: UUID): Update0 =
  sql"""insert into test (id) values ($uuid)""".update

val example = UUID.V1.next
```

```scala mdoc
val program: IO[UUID] = for {
  _ <- insert(example).run.transact(transactor)
  u <- select(example).unique.transact(transactor)
} yield u

program.unsafeRunSync
```

### Circe

```scala
libraryDependencies += "com.47deg" % "memeid-circe" % "@VERSION@"
```

You can import `memeid.circe.implicits` to have the `Encoder` and `Decoder` instances for `UUID` in scope.

```scala mdoc
import io.circe.{ Json, Encoder, Decoder }

import memeid.UUID
import memeid.circe.implicits._

val uuid = UUID.V1.next
val json = Json.fromString(uuid.toString)

Encoder[UUID].apply(uuid)
Decoder[UUID].decodeJson(json)
```

### Http4s

```scala
libraryDependencies += "com.47deg" % "memeid-http4s" % "@VERSION@"
```

### Path parameters

Using `UUID` companion object we can extract UUIDs from path parameters in URLs:

```scala mdoc:silent
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

```scala mdoc:silent
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
libraryDependencies += "com.47deg" % "memeid-cats" % "@VERSION@"
```

The cats integration provides typeclass implementation for `UUID`, as well as effectful constructors for UUIDs for integration with programs that use `cats-effect`.

### Typeclasses

```scala mdoc:silent
import cats._
import memeid.cats.implicits._

Order[UUID]
Hash[UUID]
Eq[UUID]
Show[UUID]
```

### Constructors

```scala mdoc
import memeid.cats.implicits._

UUID.random[IO].unsafeRunSync
UUID.v3[IO, String](namespace, "my-secret-code").unsafeRunSync
UUID.v5[IO, String](namespace, "my-secret-code").unsafeRunSync
```

## References

- [RFC 4122 - A Universally Unique Identifier](https://www.ietf.org/rfc/rfc4122.txt)
- JVM [UUID type](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation
