/*
 * Copyright 2019-2021 47 Degrees Open Source <https://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package memeid4s.doobie

import cats.effect._

import doobie._
import doobie.h2.implicits._
import doobie.implicits._
import doobie.specs2._
import memeid4s.UUID
import memeid4s.doobie.implicits._
import org.specs2.matcher.IOMatchers
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAll

class DoobieSpec extends Specification with IOChecker with BeforeAll with IOMatchers {

  lazy val transactor: Transactor[IO] =
    Transactor.fromDriverManager[IO](
      driver = "org.h2.Driver",
      url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      user = "",
      pass = ""
    )

  def beforeAll(): Unit =
    sql"CREATE TABLE test (id UUID NOT NULL)".update.run.transact(transactor).void.unsafeRunSync()

  def select(uuid: UUID): Query0[UUID] =
    sql"""SELECT id from test where id = $uuid""".query[UUID]

  def insert(uuid: UUID): Update0 =
    sql"""insert into test (id) values ($uuid)""".update

  check(sql"SELECT id from test".query[UUID])
  check(insert(UUID.V1.next))
  check(select(UUID.V1.next))

  "We can insert and select UUIDs" in {
    val uuid = UUID.V1.next

    val io: IO[UUID] = for {
      _ <- insert(uuid).run.transact(transactor)
      u <- select(uuid).unique.transact(transactor)
    } yield u

    io must returnValue(uuid)
  }
}
