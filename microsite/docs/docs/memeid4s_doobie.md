---
layout: docs
title: memeid4s-doobie
permalink: docs/memeid4s_doobie/
---

##### Doobie

The [Doobie](https://github.com/tpolecat/doobie) integration allows you to use the `UUID` type mapped to your database's UUID type.

```scala
libraryDependencies += "com.47deg" %% "memeid4s-doobie" % "@VERSION@"
```

To have the [UUID mappings](https://tpolecat.github.io/doobie/docs/12-Custom-Mappings.html) available in scope you can import `memeid.doobie.implicits`.

```scala mdoc:invisible
import cats.effect._

import doobie._
import doobie.h2.implicits._
import doobie.implicits._

import cats.effect.unsafe.implicits.global

val transactor: Transactor[IO] =
  Transactor.fromDriverManager[IO](
    driver = "org.h2.Driver",
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "",
    pass = ""
  )

sql"CREATE TABLE IF NOT EXISTS test (id UUID NOT NULL)".update.run.transact(transactor).unsafeRunSync()
```

```scala mdoc:silent
import memeid4s.UUID

import memeid4s.literal._
import memeid4s.doobie.implicits._

def select(uuid: UUID): Query0[UUID] =
  sql"""SELECT id from test where id = $uuid""".query[UUID]

def insert(uuid: UUID): Update0 =
  sql"""insert into test (id) values ($uuid)""".update

val example = uuid"58d61328-1b08-1171-1ee7-1283ed639e77"
```

```scala mdoc
{
  for {
    _ <- insert(example).run.transact(transactor)
    u <- select(example).unique.transact(transactor)
  } yield u
}.unsafeRunSync()
```

