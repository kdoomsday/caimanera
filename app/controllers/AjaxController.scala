package controllers

import services.user.AuthenticationEnvironment
import play.api.i18n.MessagesApi
import models.layouts.NotifyLevel
import scala.concurrent.Future
import play.api.data.Form
import play.api.data.Forms._

/** Controller para diversas respuestas de informaci&oacute;n a trav&eacute;s de ajax */
@javax.inject.Singleton
class AjaxController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment)
    extends BaseController
{
  val notifyForm = Form(
      tuple (
          "level"   → nonEmptyText,
          "message" → text
      )
  )
  
  def notification = withSession { implicit s ⇒
    Future.successful(
      notifyForm.bindFromRequest.fold(
          hasErrors ⇒ BadRequest("error"),
          data ⇒ {
            val (level, message) = data
          	Ok(views.html.util.notificar(message, NotifyLevel(level)))
          }
      )
    )
  }
}