package models.services

import scala.concurrent.Future

import models.{User, Equipo}

/**
 * Acciones de los equipos
 */
trait EquipoService {
    /** Guardar un equipo */
    def save(equipo: Equipo, manager: User): Future[Equipo]
}