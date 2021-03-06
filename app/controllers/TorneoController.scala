package controllers

import services.user.AuthenticationEnvironment
import play.api.i18n.MessagesApi
import dao.slick.TorneoDaoSlick
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import models.{ Equipo, Partido }
import java.util.UUID
import scala.concurrent.Future
import models.Torneo
import javax.inject.{ Singleton, Inject }

object TorneoController {
  case class TorneoDef(id: Long, nombre: String, idcreador: UUID, equipos: List[String])
}

@Singleton
class TorneoController @Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment )
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
  
  
  val torneoEditForm = Form (
      tuple(
          "idtorneo" → of[Long],
          "Nombre"   → nonEmptyText
      )
  )
  
  def editarTorneo(id: Long) = withAuthenticatedSession { implicit request ⇒
    torneoDao.details(id).map { maybeTorneo ⇒
      maybeTorneo match {
        case Some((t, _, _)) ⇒
          val forma: Form[(Long, String)] = torneoEditForm.fill(t.id → t.nombre)
          Ok(views.html.torneo.editarTorneo(forma))
          
        case None ⇒
          Redirect(routes.TorneoController.showTorneos()).flashing("error" → messagesApi("torneoController.noHayTorneo"))
      }
    }
  }
  
  def torneoEdit = withAuthenticatedSession { implicit request ⇒
    torneoEditForm.bindFromRequest().fold(
        errorData ⇒ {
          println(errorData)
          Future.successful(BadRequest(views.html.torneo.editarTorneo(errorData)))
        },
        data ⇒ {
          val (id, nuevoNombre) = data
          torneoDao.edit(id, nuevoNombre).map { success ⇒
            val flash = 
              if (success) "success" → messagesApi("torneoController.torneoEditado")
              else         "error"   → messagesApi("torneoController.errorEditandoTorneo")
            Redirect(routes.TorneoController.torneoDetails(id)).flashing(flash)
          }
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
      case Some((t, es, ps)) ⇒ Ok(views.html.torneo.torneoDetails(t, es, ps))
      case None ⇒ Redirect(routes.TorneoController.showTorneos()).flashing("error" → messagesApi("torneoController.noHayTorneo"))
    })
  }
  

  val partidoForm = Form(
    tuple(
      "idpartido"      → of[Long],
      "idtorneo"       → of[Long],
      "idcasa"         → of[Long],
      "scorecasa"      → number(min = 0),
      "idvisitante"    → of[Long],
      "scorevisitante" → number(min = 0),
      "fecha"          → jodaDate("yyyy-MM-dd HH:mm")
    ) verifying(messagesApi("partidoform.equiposDistintos"), fields ⇒ fields match {
      case (_, _, idcasa, _, idvisitante, _, _) => idcasa != idvisitante
    })
  )

  
  def registrarPartido(idtorneo: Long) = withAuthenticatedSession { implicit request ⇒
    torneoDao.equiposTorneo(idtorneo).map(es ⇒ Ok(views.html.torneo.registrarPartido(idtorneo, es, partidoForm)))
  }

  def registrarAction(idtorneo: Long) = withAuthenticatedSession { implicit request ⇒
    partidoForm.bindFromRequest.fold(
      hasErrors ⇒ torneoDao.equiposTorneo(idtorneo).map(es ⇒
        BadRequest(views.html.torneo.registrarPartido(idtorneo, es, hasErrors))),
      success ⇒ {
        val (idpartido, idtorneo, idcasa, scorecasa, idvisitante, scorevisitante, fecha) = success
        val p = Partido.tupled(success)
        torneoDao.agregarPartido(p).map { worked ⇒
          val flash = if (worked) "success" → messagesApi("torneoController.agregarPartido.exito")
                      else "error" → messagesApi("torneoController.agregarPartido.error")
          Redirect(routes.TorneoController.torneoDetails(idtorneo)).flashing(flash)
        }
      }
    )
  }
}