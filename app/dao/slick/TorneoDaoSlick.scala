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
  
  
  override def details(id: Long): Future[Option[(Torneo, Seq[Equipo])]] = db.run {
    val first = for {
                  (t, e) ← torneos join equipos if t.id === id
                } yield (t, e)
    
    val resultAction = first.result
    
    resultAction.map { tes ⇒
      tes.headOption map { case (t, e) ⇒
        (t, tes.map{case (t2, e2) ⇒ e2 })
      }
    }
  }
}