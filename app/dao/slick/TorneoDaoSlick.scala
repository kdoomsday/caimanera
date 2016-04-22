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
import models.Equipo
import models.Partido

class TorneoDaoSlick extends TorneoDao
  with HasDatabaseConfig[JdbcProfile]
  with SlickDBTables
{
  lazy val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  private implicit val eContext: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(5))


  private[this] def equipoIdQuery(id: Long) = equipos.filter(_.id === id).result.headOption


  override def byUser(id: UUID): Future[Seq[Torneo]] = db.run(torneos.filter { _.idcreador === id }.result)

  override def add(t: Torneo, es: Seq[Equipo]): Future[Option[Int]] = {
    val actionInsertTorneo = (torneos returning torneos.map(_.id)) += t
    val endAction = actionInsertTorneo.flatMap { idt ⇒
      equipos ++= es.map(e ⇒ e.copy(idtorneo = idt))
    }

    db.run(endAction)
  }

  override def edit(idtorneo: Long, nuevoNombre: String): Future[Boolean] = {
    val q = torneos.filter(_.id === idtorneo).map(t ⇒ t.nombre)
    db.run(q.update(nuevoNombre).map(amnt ⇒ amnt == 1))
  }

  override def eliminar(id: Long): Future[Boolean] = db.run(torneos.filter{ _.id === id }.delete.map { _ == 1 })

  override def eliminarEquipo(id: Long): Future[Boolean] =
    db.run(equipos.filter{ _.id === id }.delete.map { amnt => amnt == 1 })


  private[this] def byId(id: Long): Future[Option[Torneo]] = db.run(torneos.filter(_.id === id).result.headOption)

  private[this] def equiposDeTorneo(idtorneo: Long): Future[Seq[Equipo]] =
    db.run(equipos.filter(_.idtorneo === idtorneo).result)


  override def details(id: Long): Future[Option[(Torneo, Seq[Equipo], Seq[PartidoInfo])]] = {
    val torneoF = byId(id)

    torneoF.flatMap { tOpt ⇒
      val fr = tOpt.map { torneo ⇒
                 val es = equiposDeTorneo(id)
                 val pinfos = partidoInfos(id)

                 val fres = for {
                              equipos ← es
                              partidoInfos ← pinfos
                            } yield (torneo, equipos, partidoInfos)
                 fres
               }
      transform(fr)
    }
  }


  /** Transform an Option[Future[X]] into a Future[Option[X]] */
  private[this] def transform[A](o: Option[Future[A]]): Future[Option[A]] =
    o.map(f => f.map(Option(_))).getOrElse(Future.successful(None))

  /** PartidoInfos de un torneo */
  private[this] def partidoInfos(idtorneo: Long): Future[Seq[PartidoInfo]] = db.run {
    (for {
      partido   ← partidos if partido.idtorneo === idtorneo
      casa      ← equipos.filter(_.id === partido.idcasa)
      visitante ← equipos.filter(_.id === partido.idvisitante)
    } yield (partido, casa, visitante)).result
  }


  override def actualizarEquipo(idequipo: Long, nombre: String): Future[Boolean] = {
    val f = db.run {
      val q = equipos.filter { _.id === idequipo }.map(_.nombre)
      q.update(nombre)
    }

    f.map(x ⇒ x == 1)
  }

  override def equipoById(idequipo: Long): Future[Option[Equipo]] =
    db.run { equipoIdQuery(idequipo) }


  override def addEquipo(nombre: String, idtorneo: Long): Future[Boolean] = {
    db.run {
      equipos += Equipo(-1L, nombre, idtorneo)
    }.map { amnt => amnt == 1 }
  }


  override def equiposTorneo(idtorneo: Long): Future[Seq[Equipo]] = db.run {
    equipos.filter { _.idtorneo ===  idtorneo }.result
  }


  override def partidosTorneo(idtorneo: Long): Future[Seq[Partido]] = db.run {
    partidos.filter { _.idtorneo === idtorneo }.result
  }

  override def agregarPartido(p: Partido): Future[Boolean] = {
    db.run {
      partidos += p
    }.map(_ == 1)
  }
}
