/*
 * Copyright 2019-2022 47 Degrees Open Source <https://www.47deg.com>
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
import cats.effect.unsafe.IORuntime
import cats.effect.unsafe.implicits.global

import _root_.doobie.h2.implicits._
import _root_.doobie.implicits._
import _root_.memeid4s.UUID
import _root_.memeid4s.doobie.implicits._
import _root_.munit._
import doobie.Query0
import doobie.Transactor
import doobie.Update0

class DoobieSpec extends FunSuite with doobie.munit.IOChecker {

  lazy val transactor: Transactor[IO] =
    Transactor.fromDriverManager[IO](
      driver = "org.h2.Driver",
      url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      user = "",
      pass = ""
    )

  override def beforeAll(): Unit =
    sql"CREATE TABLE test (id UUID NOT NULL)".update.run.transact(transactor).void.unsafeRunSync()(IORuntime.global)

  def select(uuid: UUID): Query0[UUID] =
    sql"""SELECT id from test where id = $uuid""".query[UUID]

  def insert(uuid: UUID): Update0 =
    sql"""insert into test (id) values ($uuid)""".update

  test("SELECT") {
    check(sql"SELECT id from test".query[UUID])
  }
  test("Insert UUID V1") {
    check(insert(UUID.V1.next))
  }
  test("Select UUID V1") {
    check(select(UUID.V1.next))
  }

  test("We can insert and select UUIDs") {
    val uuid = UUID.V1.next

    val io: IO[UUID] = for {
      _ <- insert(uuid).run.transact(transactor)
      u <- select(uuid).unique.transact(transactor)
    } yield u

    assertEquals(io.unsafeRunSync(), uuid)
  }

}
