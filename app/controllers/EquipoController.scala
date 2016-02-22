package controllers

import services.user.AuthenticationEnvironment
import play.api.i18n.MessagesApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import scala.concurrent.Future
import views.html.torneo.torneoDetails
import dao.TorneoDao


class EquipoController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment,
    val dao: TorneoDao)
    extends BaseController
{
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
  

  
  /** Contrato: Si todo sale bien devuelve "ok" y si no "error" en data. */
  def addEquipo = withAuthenticatedSession { implicit request ⇒
    equipoForm.bindFromRequest.fold(
        hasErrors ⇒ Future.successful(BadRequest("error")),
        
        data ⇒ {
          val (idequipo, nombre, idtorneo) = data
          dao.addEquipo(nombre, idtorneo).map { res ⇒
          	if (res) Ok("ok")
          	else BadRequest("error insertando equipo")
          }
        }
    )
  }
  
  def addEquipoSuccess(idtorneo: Long) = withAuthenticatedSession { implicit request ⇒
    Future.successful(Redirect(routes.TorneoController.torneoDetails(idtorneo)).flashing(
        "success" → messagesApi("equipoController.equipoAgregado")
    ))
  }
  
  def eliminarEquipo(idtorneo: Long, idequipo: Long) = withAuthenticatedSession { implicit request ⇒
    dao.eliminarEquipo(idequipo).map { res ⇒
      val flash = if (res) "success" → messagesApi("equipoController.equipoEliminado")
                  else     "error"   → messagesApi("equipoController.errorEliminarEquipo")
      Redirect(routes.TorneoController.torneoDetails(idtorneo)).flashing(flash)
    }
  }
}