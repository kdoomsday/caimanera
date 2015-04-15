package controllers

import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator

import scala.concurrent.Future

import javax.inject.Inject
import java.util.UUID

import forms.EquipoForm
import models.{User, Equipo}
import models.services.EquipoService

/**
 * Manejar acciones de creaci&oacute;n de equipos.
 * User: Eduardo Barrientos
 * Date: 8/30/14
 * Time: 10:06 PM
 */
class CrearEquipoController @Inject() (
    val equipoService: EquipoService)
    (implicit val env: Environment[User, CachedCookieAuthenticator])
    extends Silhouette[User, CachedCookieAuthenticator]
{

    def crearEquipo = SecuredAction.async { implicit request =>
        EquipoForm.form.bindFromRequest.fold(
            form => Future.successful(BadRequest(views.html.registrarEquipo(request.identity, form))),
            data => {
                val e = Equipo(UUID.randomUUID(), data.nombre)
                equipoService.save(e, request.identity)
                Future.successful(Redirect(routes.ApplicationController.index))
            }
        )
    }
}
