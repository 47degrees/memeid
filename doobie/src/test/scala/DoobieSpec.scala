package memeid.doobie

import cats.effect._
import cats.syntax.functor._

import doobie._
import doobie.h2.implicits._
import doobie.implicits._
import doobie.specs2._
import memeid.UUID
import memeid.doobie.implicits._
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

  val uuid: UUID = UUID.v1[IO].unsafeRunSync

  def beforeAll(): Unit =
    sql"CREATE TABLE test (id UUID NOT NULL)".update.run.transact(transactor).void.unsafeRunSync

  def select(uuid: UUID): Query0[UUID] =
    sql"""SELECT id from test where id = ${uuid}""".query[UUID]

  def insert(uuid: UUID): Update0 =
    sql"""insert into test (id) values ($uuid)""".update

  check(sql"SELECT id from test".query[UUID])
  check(insert(uuid))
  check(select(uuid))

  "We can insert and select UUIDs" in {
    val io: IO[UUID] = for {
      _ <- insert(uuid).run.transact(transactor)
      u <- select(uuid).unique.transact(transactor)
    } yield u

    io must returnValue(uuid)
  }
}
