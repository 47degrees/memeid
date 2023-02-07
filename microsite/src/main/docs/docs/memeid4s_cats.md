---
layout: docs
title: memeid4s-cats
permalink: docs/memeid4s_cats/
---

##### Cats & Cats-effect

```scala
libraryDependencies += "com.47deg" %% "memeid4s-cats" % "@VERSION@"
```

The cats integration provides typeclass implementation for `UUID`, as well as effectful constructors for UUIDs for integration with programs that use `cats-effect`.

###### Typeclasses

```scala mdoc:silent
import cats._

import cats.effect.IO
import memeid4s.UUID
import memeid4s.literal._
import memeid4s.cats.implicits._

Order[UUID]
Hash[UUID]
Eq[UUID]
Show[UUID]
```

###### Constructors

```scala mdoc:silent
val namespace = UUID.V1.next

UUID.random[IO]
UUID.v3[IO, String](namespace, "my-secret-code")
UUID.v5[IO, String](namespace, "my-secret-code")
```

