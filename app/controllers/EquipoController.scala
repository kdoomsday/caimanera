package controllers

import services.user.AuthenticationEnvironment
import play.api.i18n.MessagesApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import scala.concurrent.Future
import views.html.torneo.torneoDetails
import dao.slick.TorneoDaoSlick

class EquipoController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment)
    extends BaseController
{
  val dao = new TorneoDaoSlick()
  implicit val execContext = env.executionContext
  
  
  val equipoForm = Form(
      tuple(
    		  "idequipo" → of[Long],
          "Nombre"   → nonEmptyText,
          "idtorneo" → of[Long]
      )
  )
  
  
  def editarEquipoView(idtorneo: Long, idequipo: Long) = withAuthenticatedSession { implicit request ⇒
    dao.equipoById(idequipo).map { oe ⇒ oe match {
      case Some(equipo) ⇒
        val data = (equipo.id, equipo.nombre, equipo.idtorneo)
        Ok(views.html.torneo.editarEquipo(equipoForm.fill(data)))
        
      case None ⇒
        Redirect(routes.TorneoController.torneoDetails(idtorneo)).flashing("error" → messagesApi("equipoController.equipoNoExiste"))
    }}
  }
  
  
  def editarEquipo = withAuthenticatedSession { implicit request ⇒
    equipoForm.bindFromRequest().fold (
      hasErrors ⇒ Future.successful(BadRequest(views.html.torneo.editarEquipo(hasErrors))),
      
      data ⇒ {
        val (idequipo, nombre, idtorneo) = data
        dao.actualizarEquipo(idequipo, nombre).map { res ⇒
          val flash = if (res) ("success" → messagesApi("equipoController.equipoEditado"))
                      else     ("error"   → messagesApi("equipoController.errorEdicionEquipo"))
          Redirect(routes.TorneoController.torneoDetails(idtorneo)).flashing(flash)
        }
      }
    )
  }
}