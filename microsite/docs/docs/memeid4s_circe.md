---
layout: docs
title: memeid-4scirce
permalink: docs/memeid4s_circe/
---

##### Circe

```scala
libraryDependencies += "com.47deg" %% "memeid4s-circe" % "@VERSION@"
```

You can import `memeid.circe.implicits` to have the `Encoder` and `Decoder` instances for `UUID` in scope.

```scala mdoc:silent
import memeid4s.UUID

import memeid4s.literal._
import io.circe.Json
import io.circe.Encoder
import io.circe.Decoder
import memeid4s.circe.implicits._

val uuid = uuid"58d61328-1b08-1171-1ee7-1283ed639e77"

val json = Json.fromString(uuid.toString)
```

```scala mdoc
Encoder[UUID].apply(uuid)
Decoder[UUID].decodeJson(json)
```

