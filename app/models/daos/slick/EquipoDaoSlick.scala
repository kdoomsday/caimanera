package models.daos.slick

import java.util.UUID

import com.mohiva.play.silhouette.core.LoginInfo
import models.daos.{UserDAO, EquipoDAO}
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
class EquipoDaoSlick(val userDao: UserDAO)(implicit session: Session) extends EquipoDAO {
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
  override def save(equipo: Equipo): Future[Equipo] = ???


  /* *** Conversiones de tipos de db a tipos del modelo *** */

  /** Convertir de DBEquipo a Equipo. */
  private[this] def dbEquipo2Equipo(dbe: DBEquipo): Equipo = Equipo(UUID.fromString(dbe.id), dbe.nombre)

  private[this] def dbUser2User(dbu: DBUser): User = {
    slickUserLoginInfos.filter(_.userID === dbu.userID).firstOption match {
      case Some(info) =>
        slickLoginInfos.filter(_.id === info.loginInfoId).firstOption match {
          case Some(loginInfo) =>
            User(
              UUID.fromString(dbu.userID),
              LoginInfo(loginInfo.providerID, loginInfo.providerKey),
              dbu.firstName,
              dbu.lastName,
              dbu.fullName,
              dbu.email,
              dbu.avatarURL)

          case _ => throw new Exception("User without login info")
        }

      case _ => throw new Exception("User without user login info")
    }
  }
}