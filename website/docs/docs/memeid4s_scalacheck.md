---
layout: docs
title: memeid4s-scalacheck
permalink: docs/memeid4s_scalacheck/
---

#### Scalacheck

```scala
libraryDependencies += "com.47deg" %% "memeid4s-scalacheck" % "@VERSION@"
```

The scalacheck integration provides `Arbitrary` instances for the `UUID`, as well as for the different version classes.

```scala
import org.scalacheck.Arbitrary.arbitrary
import memeid4s.UUID
import memeid4s.scalacheck.arbitrary.instances._

arbitrary[UUID]
arbitrary[UUID.V1]
arbitrary[UUID.V2]
arbitrary[UUID.V3]
arbitrary[UUID.V4]
arbitrary[UUID.V5]
```