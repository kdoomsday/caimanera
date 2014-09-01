package models.daos.slick

import java.util.UUID

import models.daos.EquipoDAO
import models.{Equipo, User}
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBTableDefinitions._

import scala.concurrent.Future

/**
 * User: Eduardo Barrientos
 * Date: 8/31/14
 * Time: 1:30 PM
 */
class EquipoDaoSlick extends EquipoDAO {
  import play.api.Play.current

  /** Obtener un equipo dado su id. */
  override def find(id: UUID): Future[Option[Equipo]] = DB.withSession { implicit  session =>
    Future.successful(slickEquipos.filter(_.id === id.toString).firstOption.map(dbEquipo2Equipo))
  }
  
  /** Obtener todos los equipos que tienen un nombre en particular. */
  override def findByNombre(nombre: String): Future[Seq[Equipo]] = DB withSession { implicit session =>
    Future.successful {
      slickEquipos.filter(_.nombre === nombre).list.map(dbEquipo2Equipo)
    }
  }

  /** Obtener los jugadores de un equipo. */
  override def jugadores(equipo: Equipo): Future[Seq[User]] = DB.withSession { implicit session =>
    val query = for {
                  (ide, idu, _) <- slickJugadorEquipo if ide === equipo.equipoID.toString
                  user <- slickUsers if user.userID === idu
                } yield user
    Future.successful(query.list)
  }

  /** Guardar el equipo. */
  override def save(equipo: Equipo): Future[Equipo] = ???


  /** Convertir de DBEquipo a Equipo. */
  private[this] def dbEquipo2Equipo(dbe: DBEquipo): Equipo = Equipo(UUID.fromString(dbe.id), dbe.nombre)
}
