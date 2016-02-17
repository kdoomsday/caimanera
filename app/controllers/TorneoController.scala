package controllers

import services.user.AuthenticationEnvironment
import play.api.i18n.MessagesApi
import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import play.api.Play
import dao.slick.TorneoDaoSlick
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import models.{ Torneo, Equipo }
import java.util.UUID
import scala.concurrent.Future

object TorneoController {
  case class TorneoDef(id: Long, nombre: String, idcreador: UUID, equipos: List[String])
}

@javax.inject.Singleton
class TorneoController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment)
    extends BaseController
{
  import TorneoController.TorneoDef
  
  private val torneoDao = new TorneoDaoSlick()

  implicit val execContext = env.executionContext

  def showTorneos = withAuthenticatedSession { implicit request ⇒
    torneoDao.byUser(request.identity.id).map { s ⇒ Ok(views.html.torneo.showTorneos(s)) }
  }
  
  
  val torneo = Form(
    mapping (
    	"idtorneo" → of[Long],
      "Nombre" → nonEmptyText,
      "idcreador" → of[UUID],
      "equipo" → list(nonEmptyText)
    )(TorneoDef.apply)(TorneoDef.unapply)
  )
  
  def createTorneo = withAuthenticatedSession { implicit request ⇒
    Future.successful(Ok(views.html.torneo.crearTorneo(torneo, request.identity)))
  }
  
  
  def torneoAdd = withAuthenticatedSession { implicit request ⇒ 
    torneo.bindFromRequest().fold(
      hasErrors ⇒
        Future.successful(
            BadRequest(views.html.torneo.crearTorneo(hasErrors, request.identity))),
        
      data ⇒ {
        val (t, es) = (Torneo(data.id, data.nombre, data.idcreador), data.equipos)
        val equipos = es.map(nom ⇒ Equipo(-1L, nom, t.id))
        
        println(equipos)
        
        torneoDao.add(t, equipos).map { _ ⇒
          Redirect(routes.TorneoController.showTorneos())
            .flashing("success" → messagesApi("torneoController.exitoCrearTorneo")) }
      }
    )
  }
  
  def torneoRemove(id: Long) = withAuthenticatedSession { implicit request ⇒
    torneoDao.eliminar(id).map { res ⇒
      if (res) Redirect(routes.TorneoController.showTorneos()).flashing("info" → messagesApi("torneoEliminado"))
      else     Redirect(routes.TorneoController.showTorneos()).flashing("error" → messagesApi("torneoEliminadoWrong"))
    }
  }
  
  def torneoDetails(id: Long) = withAuthenticatedSession { implicit request ⇒
    torneoDao.details(id).map( oDetails ⇒ oDetails match {
      case Some((t, es)) ⇒ Ok(views.html.torneo.torneoDetails(t, es))
      case None ⇒ Redirect(routes.TorneoController.showTorneos()).flashing("error" → messagesApi("torneoController.noHayTorneo"))
    })
  }
}