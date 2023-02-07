---
layout: docs
title: memeid4s-tapir
permalink: docs/memeid4s_tapir/
---

##### Tapir

```scala
libraryDependencies += "com.47deg" %% "memeid4s-tapir" % "@VERSION@"
```

The [Tapir](https://tapir.softwaremill.com/en/latest/) integration provides implicit instances for `Codec[UUID]` and `Schema[UUID]`, which allow using `UUID` as 
type for query/path params or headers in endpoints. As well as enriching documentation when a `UUID` field is used.

```scala mdoc:silent
import memeid4s.UUID

import memeid4s.tapir.implicits._

import sttp.tapir._

endpoint.get.in("hello" / path[UUID])
```
