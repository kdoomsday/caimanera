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
import models.Torneo
import java.util.UUID
import scala.concurrent.Future
import models.Torneo

@javax.inject.Singleton
class TorneoController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment)
    extends BaseController
{
  private val torneoDao = new TorneoDaoSlick()

  implicit val execContext = env.executionContext

  def showTorneos = withAuthenticatedSession { implicit request ⇒
    torneoDao.byUser(request.identity.id).map { s ⇒ Ok(views.html.torneo.showTorneos(s)) }
  }
  
  val torneo = Form(
    mapping (
    	"idtorneo" → of[Long],
      "Nombre" → nonEmptyText,
      "idcreador" → of[UUID]
    )(Torneo.apply)(Torneo.unapply)
  )
  
  def createTorneo = withAuthenticatedSession { implicit request ⇒
    Future.successful(Ok(views.html.torneo.crearTorneo(torneo, request.identity)))
  }
  
  
  def torneoAdd = withAuthenticatedSession { implicit request ⇒ 
    torneo.bindFromRequest().fold(
      hasErrors ⇒
        Future.successful(
            BadRequest(views.html.torneo.crearTorneo(hasErrors, request.identity))),
        
      data ⇒
        torneoDao.add(data).map { _ => Redirect(routes.TorneoController.showTorneos()) }
    )
  }
  
  def torneoRemove(id: Long) = withAuthenticatedSession { implicit request ⇒
    torneoDao.eliminar(id).map { amnt ⇒ amnt match {
      case 1 ⇒ Redirect(routes.TorneoController.showTorneos()).flashing("info" → messagesApi("torneoEliminado"))
      case _ ⇒ Redirect(routes.TorneoController.showTorneos()).flashing("error" → messagesApi("torneoEliminadoWrong"))
    }}
  }
}