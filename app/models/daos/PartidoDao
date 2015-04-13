package models.daos

import models.{Partido, Equipo}

import scala.concurrent.Future

import java.util.UUID


/** Dao de partidos */
trait PartidoDao {

    /** Obtener un partido dado su id. */
  def find(id: UUID): Future[Option[Partido]]
}