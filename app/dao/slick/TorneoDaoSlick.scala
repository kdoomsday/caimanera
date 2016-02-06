package dao.slick

import dao.TorneoDao
import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile
import scala.concurrent.Future
import models.Torneo
import play.api.db.slick.DatabaseConfigProvider
import play.api.Play
import tables.SlickDBTables
import tables.SlickDBTables
import java.util.UUID

class TorneoDaoSlick extends TorneoDao
  with HasDatabaseConfig[JdbcProfile]
  with SlickDBTables
{
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  
  override def first(n: Int): Future[Seq[Torneo]] = db.run(torneos.take(n).result)
  
  override def byUser(id: UUID): Future[Seq[Torneo]] = db.run(torneos.filter { _.idcreador === id }.result)
  
  override def add(t: Torneo): Future[Unit] = db.run{ DBIO.seq((torneos += t)) }
}