---
layout: docs
title: memeid4s-fuuid
permalink: docs/memeid4s_fuuid/
---

##### FUUID

```scala
libraryDependencies += "com.47deg" %% "memeid4s-fuuid" % "@VERSION@"
```

The [FUUID](https://christopherdavenport.github.io/fuuid/) integration provides both semi (via extension methods) and auto conversions between memeid's `UUID` type and `FUUID`.

```scala mdoc:silent
import memeid4s.UUID
import io.chrisdavenport.fuuid.FUUID
import memeid4s.fuuid.syntax._

val fuuid: FUUID = UUID.V4.random.toFUUID

val uuid: UUID = fuuid.toUUID
```

```scala mdoc:reset:silent
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
