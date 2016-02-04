package dao.slick

import dao.TorneoDao
import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile
import tables.TorneoTable
import scala.concurrent.Future
import models.Torneo
import play.api.db.slick.DatabaseConfigProvider
import play.api.Play
import tables.TorneoTable

class TorneoDaoSlick extends TorneoDao
  with HasDatabaseConfig[JdbcProfile]
  with TorneoTable
{
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  private[this] val torneos = TableQuery[Torneos]

  override def first(n: Int): Future[Seq[Torneo]] = db.run(torneos.take(n).result)
}