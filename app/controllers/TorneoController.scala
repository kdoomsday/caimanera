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

@javax.inject.Singleton
class TorneoController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment)
    extends BaseController
{
  private val torneoDao = new TorneoDaoSlick()

  implicit val execContext = env.executionContext

  def showTorneos = withSession { implicit request ⇒
    torneoDao.first(10).map { s => Ok(views.html.torneo.showTorneos(s)) }
  }
  
  val torneo = Form(
    mapping (
    	"idtorneo" → of[Long],
      "Nombre" → nonEmptyText,
      "idcreador" → of[UUID]
    )(Torneo.apply)(Torneo.unapply)
  )
  
  def createTorneo = withSession { implicit request ⇒
    Future.successful(Ok(views.html.torneo.crearTorneo(torneo)))
  }
}