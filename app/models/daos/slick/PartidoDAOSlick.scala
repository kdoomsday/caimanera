package models.daos.slick

import java.util.UUID
import models.daos.slick.DBTableDefinitions.DBPartido
import models.{Partido, Equipo}
import models.daos.{EquipoDAO, PartidoDao}
import org.joda.time.DateTime
import scala.concurrent.Future
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBTableDefinitions.slickPartidos
import javax.inject.Inject
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.Formatter.DateTime

/**
 * User: Eduardo Barrientos
 * Date: 10/17/15
 * Time: 12:57 PM
 */
class PartidoDAOSlick @Inject() (private val daoEquipo: EquipoDAO) extends PartidoDao {
  
  /* Query que busca todos los partidos de un equipo */
  private[this] def todosDeUnEquipo(e: Equipo) = {
    val id = e.equipoID.toString
    for {
      p <- slickPartidos
      if p.idCasa === id || p.idVisitante === id
    } yield p
  }


  /* Convertir de DBPartido a Partido. Es Future porque hay consultas a bd */
  private[this] def dbp2p(dbp: DBPartido): Future[Partido] = DB.withSession { implicit session =>
    val fCasa = daoEquipo.find(UUID.fromString(dbp.idCasa))
    val fVisitante = daoEquipo.find(UUID.fromString(dbp.idVisitante))
    
    for {
      oCasa <- fCasa
      oVisitante <- fVisitante
      casa = oCasa.get            // No deberia fallar porque esta registrado. Si falla que explote
      visitante = oVisitante.get
    } yield Partido(
              UUID.fromString(dbp.id),
              casa,
              visitante,
              dbp.golCasa,
              dbp.golVisitante,
              new DateTime(dbp.fecha))
    
  }


  /* Obtener un partido dado su id. */
  override def find(id: UUID): Future[Option[Partido]] = DB.withSession { implicit session =>
    val dbpOpt = slickPartidos.filter(_.id === id.toString).firstOption

    dbpOpt match {
      case Some(dbp) => for (p <- dbp2p(dbp)) yield Some(p)
      case None => Future.successful(None)
    }
  }

  
  /* Ultimos n partidos en los que participo un equipo */
  override def ultimosPartidos(e: Equipo, cantidad: Int): Future[Seq[Partido]] =
    DB.withSession { implicit session =>
      val lOfFutures = for {
                          dbp <- todosDeUnEquipo(e).sortBy { p => p.fecha }.take(cantidad).list
                          p = dbp2p(dbp)
                        } yield p
      Future.sequence(lOfFutures)
    }
}
