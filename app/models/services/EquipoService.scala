package models.services

import scala.concurrent.Future

import models.Equipo

/**
 * Acciones de los equipos
 */
trait EquipoService {
    /** Guardar un equipo */
    def save(equipo: Equipo): Future[Equipo]
}