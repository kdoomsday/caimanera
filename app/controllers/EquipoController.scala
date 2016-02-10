package controllers

import services.user.AuthenticationEnvironment
import play.api.i18n.MessagesApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import scala.concurrent.Future
import views.html.torneo.torneoDetails

class EquipoController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment)
    extends BaseController
{
  val equipoForm = Form(
      tuple(
          "Nombre"   → nonEmptyText,
          "idequipo" → of[Long],
          "idtorneo" → of[Long]
      )
  )
  
  
  def editarEquipoView(idequipo: Long) = withAuthenticatedSession { implicit request ⇒
    
    Future.successful(Ok(views.html.torneo.editarEquipo(equipoForm.)))
  }
  
  
  def editarEquipo = withAuthenticatedSession { implicit request ⇒
    equipoForm.bindFromRequest().fold (
      hasErrors ⇒ Future.successful(BadRequest(views.html.torneo.editarEquipo(hasErrors))),
      
      data ⇒ {
        Future.successful(Ok(routes.TorneoController.torneoDetails(data._3)))
      }
    )
  }
}