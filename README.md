\![](design-assets/other-file-formats/readme-header.png)

`memeid` is a JVM library for generating [RFC-compliant](https://www.ietf.org/rfc/rfc4122.txt) Universal Unique Identifiers (UUIDs).

> A universally unique identifier (UUID) is a 128-bit number used to identify information in computer systems.
>
> When generated according to the standard methods, UUIDs are for practical purposes unique. Their uniqueness does not depend on a central registration authority or coordination between the parties generating them, unlike most other numbering schemes.
-- [Wikipedia article on UUIDs](https://en.wikipedia.org/wiki/Universally_unique_identifier)

---

- [Rationale](#rationale)
- [Install](#install)
  - [Java](#java)
    - [Using maven](#using-maven)
    - [Using gradle](#using-gradle)
  - [Scala](#scala)
- [Usage](#usage)
  - [Scala](#scala)
    - [UUID construction](#uuid-construction)
      - [Time-based (v1)](#time-based-v1)
      - [Random (v4)](#random-v4)
      - [Namespaced (v3, v5)](#namespaced-v3-v5)
      - [Semi-sequential, random (SQUUID)](#semi-sequential-random-squuid)
    - [Java interoperability](#java-interoperability)
    - [Literal syntax](#literal-syntax)
    - [Integrations](#integrations)
      - [Doobie](#doobie)
      - [Circe](#circe)
      - [Http4s](#http4s)
        - [Path parameters](#path-parameters)
        - [Query parameters](#query-parameters)
      - [Tapir](#tapir)
      - [FUUID](#fuuid)
      - [Cats & Cats-effect](#cats--cats-effect)
        - [Typeclasses](#typeclasses)
        - [Constructors](#constructors)
      - [Scalacheck](#scalacheck)
- [Benchmarking](#benchmarking)
- [References](#references)

## Rationale

The UUID type that ships with the JVM `java.util.UUID` has a number of problems, namely:

 - [A bug in the comparison function that will never be fixed](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=7025832) 
 - Only provides UUID generation for random (V4) and non-namespaced pseudo-V3 UUIDs

This library aims to solve the aforementioned issues and provide an RFC-compliant `UUID` type with coherent comparison, a rich API to get its different fields and constructors for the UUID variants.

## Install

### Java

#### Using maven

```xml
<dependency>
    <groupId>com.47deg</groupId>
    <artifactId>memeid</artifactId>
    <version>0.7.0</version>
</dependency>
```

#### Using gradle

```groovy
compile group: 'com.47deg', name: 'memeid', version: '0.7.0'
```

### Scala

Add this to your `build.sbt` file:

```scala
libraryDependencies += "com.47deg" %% "memeid4s" % "0.7.0"
```

## Usage

### Scala

#### UUID construction

##### Time-based (v1)

The time-based (V1) variant of UUIDs is the fastest to generate. It uses a monotonic clock and node information to generate UUIDs.

```scala
import memeid4s.UUID

UUID.V1.next
```

##### Random (v4)

The cryptographically random variant, equivalent to `java.util.UUID/randomUUID`.

```scala
UUID.V4.random
```

##### Namespaced (v3, v5)

Namespaced UUIDs are generated from a UUID (namespace) and a hashed value (name), V3 uses MD5 and V5 uses SHA1 hash.

```scala
val namespace = UUID.V1.next
```

We can now create UUIDs with the namespace and an arbitrary value as the name. It automatically works with Strings and UUIDs:

```scala
UUID.V3(namespace, "my-secret-code")
```

If you want to hash a custom type, you must provide an implicit `memeid4s.digest.Digestible` instance.

```scala
import memeid4s.digest.Digestible

case class User(firstName: String, lastName: String)

implicit val digestibleUser: Digestible[User] =
  (u: User) => u.firstName.getBytes ++ u.lastName.getBytes
```

The implicit instance is used to convert your type into a byte array for hashing:

```scala
UUID.V3(namespace, User("Federico", "García Lorca"))
```

##### Semi-sequential, random (SQUUID)

SQUUIDs are a non-standard variaton of V4 UUIDs that are semi-sequential. They incorporate a time-component in their 32 most significant bits to generate UUIDs that don't fragment DB indexes.

```scala
UUID.V4.squuid
```

#### Java interoperability

`memeid` provides conversion method between `UUID` and `java.util.UUID` through:

```scala
val j = java.util.UUID.fromString("a5fa7934-501c-46eb-9ea7-16de3086e6d8")

val u = memeid.UUID.fromString("8b4d1529-5fd0-4a91-8f4f-ceee10d1c060")
```

```scala
UUID.fromUUID(j)
// res5: UUID = a5fa7934-501c-46eb-9ea7-16de3086e6d8

u.asJava
// res6: java.util.UUID = 8b4d1529-5fd0-4a91-8f4f-ceee10d1c060
```

#### Literal syntax

`memeid` provides literal syntax with compile-time verification for UUIDs with the `uuid` interpolator. To use it, add this to your `build.sbt`:


```scala
libraryDependencies += "com.47deg" %% "memeid4s-literal" % "0.7.0"
```

We can now create UUIDs with literal syntax by importing `memeid.literal._`

```scala
import memeid4s.literal._

uuid"cb096727-6a82-4abd-bc79-fc92be8c5d88"
// res7: UUID = cb096727-6a82-4abd-bc79-fc92be8c5d88
```

Invalid UUID literals will fail at compile time:

```scala
uuid"not-a-uuid"
// error: invalid UUID: not-a-uuid
// uuid"not-a-uuid"
// ^^^^^^^^^^^^^^^^
```

#### Integrations

`memeid` provides several modules which integrate with popular third-party libraries. If you see something missing don't hesitate to open an issue or send a patch.

##### Doobie

The [Doobie](https://github.com/tpolecat/doobie) integration allows you to use the `UUID` type mapped to your database's UUID type.

```scala
libraryDependencies += "com.47deg" %% "memeid4s-doobie" % "0.7.0"
```

To have the [UUID mappings](https://tpolecat.github.io/doobie/docs/12-Custom-Mappings.html) available in scope you can import `memeid.doobie.implicits`.


```scala
import memeid4s.doobie.implicits._

def select(uuid: UUID): Query0[UUID] =
  sql"""SELECT id from test where id = $uuid""".query[UUID]

def insert(uuid: UUID): Update0 =
  sql"""insert into test (id) values ($uuid)""".update

val example = uuid"58d61328-1b08-1171-1ee7-1283ed639e77"
```

```scala
{
  for {
    _ <- insert(example).run.transact(transactor)
    u <- select(example).unique.transact(transactor)
  } yield u
}.unsafeRunSync()
// res10: UUID = 58d61328-1b08-1171-1ee7-1283ed639e77
```

##### Circe

```scala
libraryDependencies += "com.47deg" %% "memeid4s-circe" % "0.7.0"
```

You can import `memeid.circe.implicits` to have the `Encoder` and `Decoder` instances for `UUID` in scope.

```scala
import io.circe.Json

import io.circe.Encoder
import io.circe.Decoder

import memeid4s.circe.implicits._

val uuid = uuid"58d61328-1b08-1171-1ee7-1283ed639e77"

val json = Json.fromString(uuid.toString)
```

```scala
Encoder[UUID].apply(uuid)
// res11: Json = JString(value = "58d61328-1b08-1171-1ee7-1283ed639e77")
Decoder[UUID].decodeJson(json)
// res12: Decoder.Result[UUID] = Right(
//   value = 58d61328-1b08-1171-1ee7-1283ed639e77
// )
```

##### Http4s

```scala
libraryDependencies += "com.47deg" %% "memeid4s-http4s" % "0.7.0"
```

###### Path parameters

Using `UUID` companion object we can extract UUIDs from path parameters in URLs:

```scala
import cats.effect._

import org.http4s._
import org.http4s.dsl.io._

HttpRoutes.of[IO] { case GET -> Root / "user" / UUID(uuid) =>
  Ok(s"Hello, $uuid!")
}
```

###### Query parameters

The http4s integrations provides implicit instances for `QueryParamDecoder[UUID]` and `QueryParamEncoder[UUID]`, which you can use to derive matchers for query parameters or send UUID in request query parameters.

```scala
import cats.effect._

import org.http4s._
import org.http4s.dsl.io._

import memeid4s.http4s.implicits._

object UUIDParamDecoder extends QueryParamDecoderMatcher[UUID]("uuid")

HttpRoutes.of[IO] { case GET -> Root / "user" :? UUIDParamDecoder(uuid) =>
  Ok(s"Hello, $uuid!")
}
```

##### Tapir

```scala
libraryDependencies += "com.47deg" %% "memeid4s-tapir" % "0.7.0"
```

The [Tapir](https://tapir.softwaremill.com/en/latest/) integration provides implicit instances for `Codec[UUID]` and `Schema[UUID]`, which allow using `UUID` as 
type for query/path params or headers in endpoints. As well as enriching documentation when a `UUID` field is used.

```scala
import memeid4s.tapir.implicits._

import sttp.tapir._

endpoint.get.in("hello" / path[UUID])
```

##### FUUID

```scala
libraryDependencies += "com.47deg" %% "memeid4s-fuuid" % "0.7.0"
```

The [FUUID](https://christopherdavenport.github.io/fuuid/) integration provides both semi (via extension methods) and auto conversions between memeid's `UUID` type and `FUUID`.

```scala
import memeid4s.UUID

import io.chrisdavenport.fuuid.FUUID
import memeid4s.fuuid.syntax._

val fuuid: FUUID = UUID.V4.random.toFUUID

val uuid: UUID = fuuid.toUUID
```

```scala
import memeid4s.UUID

import io.chrisdavenport.fuuid.FUUID
import memeid4s.fuuid.auto._

def usingFUUID(fuuid: FUUID) = fuuid

def usingUUID(uuid: UUID) = uuid

val uuid: UUID = UUID.V4.random

val fuuid: FUUID = FUUID.fromUUID(java.util.UUID.randomUUID)

usingFUUID(uuid)
usingUUID(fuuid)
```

##### Cats & Cats-effect

```scala
libraryDependencies += "com.47deg" %% "memeid4s-cats" % "0.7.0"
```

The cats integration provides typeclass implementation for `UUID`, as well as effectful constructors for UUIDs for integration with programs that use `cats-effect`.

###### Typeclasses

```scala
import cats._

import memeid4s.cats.implicits._
import cats.effect.IO

Order[UUID]
Hash[UUID]
Eq[UUID]
Show[UUID]
```

###### Constructors

```scala
UUID.random[IO]

val namespace = UUID.V4.random

UUID.v3[IO, String](namespace, "my-secret-code")
UUID.v5[IO, String](namespace, "my-secret-code")
```

##### Scalacheck

```scala
libraryDependencies += "com.47deg" %% "memeid4s-scalacheck" % "0.7.0"
```

The scalacheck integration provides `Arbitrary` instances for the `UUID`, as well as for the different version classes.

```scala
import org.scalacheck.Arbitrary.arbitrary

import memeid4s.scalacheck.arbitrary.instances._

arbitrary[UUID]
arbitrary[UUID.V1]
arbitrary[UUID.V2]
arbitrary[UUID.V3]
arbitrary[UUID.V4]
arbitrary[UUID.V5]
```

## Benchmarking

[``sbt-jmh``](https://github.com/ktoso/sbt-jmh) is used for executing the benchmarking tests. 

There are 2 kind of benchmarking:
- ``runAvgtime``: Measures the average time it takes for the benchmark method to execute (a single execution). Generates the ``master.avgtime.csv`` file in the ``bench`` folder. 
- ``runThroughput``: Measures the number of operations per second, meaning the number of times per second your benchmark method could be executed. Generates the ``master.throughput.csv`` file in the ``bench`` folder.


## References

- [RFC 4122 - A Universally Unique Identifier](https://www.ietf.org/rfc/rfc4122.txt)
- JVM [UUID type](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation
