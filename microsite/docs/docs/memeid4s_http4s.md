---
layout: docs
title: memeid4s-http4s
permalink: docs/memeid4s_http4s/
---

##### Http4s

```scala
libraryDependencies += "com.47deg" %% "memeid4s-http4s" % "@VERSION@"
```

###### Path parameters

Using `UUID` companion object we can extract UUIDs from path parameters in URLs:

```scala mdoc:silent
import cats.effect._

import org.http4s._
import org.http4s.dsl.io._
import memeid4s.UUID
import memeid4s.literal._

HttpRoutes.of[IO] { case GET -> Root / "user" / UUID(uuid) =>
  Ok(s"Hello, $uuid!")
}
```

###### Query parameters

The http4s integrations provides implicit instances for `QueryParamDecoder[UUID]` and `QueryParamEncoder[UUID]`, which you can use to derive matchers for query parameters or send UUID in request query parameters.

```scala mdoc:silent
import cats.effect._

import org.http4s._
import org.http4s.dsl.io._

import memeid4s.http4s.implicits._

object UUIDParamDecoder extends QueryParamDecoderMatcher[UUID]("uuid")

HttpRoutes.of[IO] { case GET -> Root / "user" :? UUIDParamDecoder(uuid) =>
  Ok(s"Hello, $uuid!")
}
```

