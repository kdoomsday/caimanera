package models.daos.slick

import javax.inject.Inject
import java.util.UUID

import models.daos.{UserDAO, EquipoDAO}
import models.{Equipo, User}
import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daos.slick.DBTableDefinitions._
import models.daos.slick.DBTableDefinitions.Conversions._

import java.sql.Timestamp

import scala.concurrent.Future

/**
 * User: Eduardo Barrientos
 * Date: 8/31/14
 * Time: 1:30 PM
 */
class EquipoDaoSlick @Inject() (val userDao: UserDAO) extends EquipoDAO {
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
      je <- slickJugadorEquipo if je.idequipo === equipo.equipoID.toString
      user <- slickUsers if user.id === je.iduser
    } yield user

    Future.successful(query.list.map(dbUser2User))
  }

  
  /** Guardar el equipo. */
  override def save(equipo: Equipo, manager: User): Future[Equipo] = DB.withSession { implicit session =>
    slickEquipos += equipo2DBEquipo(equipo)
    slickJugadorEquipo += (equipo.equipoID.toString, manager.userID.toString, true)
    
    Future.successful(equipo)
  }
}
