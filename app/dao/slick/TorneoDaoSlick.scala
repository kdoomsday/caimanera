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
import models.Equipo
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors

class TorneoDaoSlick extends TorneoDao
  with HasDatabaseConfig[JdbcProfile]
  with SlickDBTables
{
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._
  
  private implicit val eContext: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(5))

  
  override def byUser(id: UUID): Future[Seq[Torneo]] = db.run(torneos.filter { _.idcreador === id }.result)
  
  override def add(t: Torneo, es: Seq[Equipo]): Future[Unit] = db.run { DBIO.seq(
    torneos += t,
    equipos ++= es
  )}
  
  override def eliminar(id: Long): Future[Int] = db.run(torneos.filter{ _.id === id }.delete)
  
  
  private[this] def byId(id: Long): Future[Option[Torneo]] = db.run(torneos.filter(_.id === id).result.headOption)
  
  private[this] def equiposDeTorneo(idtorneo: Long): Future[Seq[Equipo]] =
    db.run(equipos.filter(_.idtorneo === idtorneo).result)
  
  override def details(id: Long): Future[Option[(Torneo, Seq[Equipo])]] = {
    for {
      ot ← byId(id)
      es ← equiposDeTorneo(id)
    } yield {
      ot.map(t ⇒ (t, es))
    }
  }
}