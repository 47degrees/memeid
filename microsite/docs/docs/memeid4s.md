---
layout: docs
title: memeid-4s
permalink: docs/memeid4s/
---

## Usage

### Scala

#### UUID construction

##### Time-based (v1)

The time-based (V1) variant of UUIDs is the fastest to generate. It uses a monotonic clock and node information to generate UUIDs.

```scala mdoc:silent
import memeid4s.UUID

UUID.V1.next
```

##### Random (v4)

The cryptographically random variant, equivalent to `java.util.UUID/randomUUID`.

```scala mdoc:silent
UUID.V4.random
```

##### Namespaced (v3, v5)

Namespaced UUIDs are generated from a UUID (namespace) and a hashed value (name), V3 uses MD5 and V5 uses SHA1 hash.

```scala mdoc:silent
val namespace = UUID.V1.next
```

We can now create UUIDs with the namespace and an arbitrary value as the name. It automatically works with Strings and UUIDs:

```scala mdoc:silent
UUID.V3(namespace, "my-secret-code")
```

If you want to hash a custom type, you must provide an implicit `memeid4s.digest.Digestible` instance.

```scala mdoc:silent
import memeid4s.digest.Digestible

case class User(firstName: String, lastName: String)

implicit val digestibleUser: Digestible[User] =
  (u: User) => u.firstName.getBytes ++ u.lastName.getBytes
```

The implicit instance is used to convert your type into a byte array for hashing:

```scala mdoc:silent
UUID.V3(namespace, User("Federico", "García Lorca"))
```

##### Semi-sequential, random (SQUUID)

SQUUIDs are a non-standard variaton of V4 UUIDs that are semi-sequential. They incorporate a time-component in their 32 most significant bits to generate UUIDs that don't fragment DB indexes.

```scala mdoc:silent
UUID.V4.squuid
```

#### Java interoperability

`memeid` provides conversion method between `UUID` and `java.util.UUID` through:

```scala mdoc:silent
val j = java.util.UUID.fromString("a5fa7934-501c-46eb-9ea7-16de3086e6d8")
val u = memeid.UUID.fromString("8b4d1529-5fd0-4a91-8f4f-ceee10d1c060")
```

```scala mdoc
UUID.fromUUID(j)
u.asJava
```

#### Literal syntax

`memeid` provides literal syntax with compile-time verification for UUIDs with the `uuid` interpolator. To use it, add this to your `build.sbt`:


```scala
libraryDependencies += "com.47deg" %% "memeid4s-literal" % "@VERSION@"
```

We can now create UUIDs with literal syntax by importing `memeid.literal._`

```scala mdoc
import memeid4s.literal._

uuid"cb096727-6a82-4abd-bc79-fc92be8c5d88"
```

Invalid UUID literals will fail at compile time:

```scala mdoc:fail
uuid"not-a-uuid"
```

