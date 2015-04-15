package models.daos

import java.util.UUID

import models.{User, Equipo}

import scala.concurrent.Future

/**
 * Dao para conseguir y guardar equipos
 * User: Eduardo Barrientos
 * Date: 8/30/14
 * Time: 10:06 PM
 */
trait EquipoDAO {

  /** Obtener un equipo dado su id. */
  def find(id: UUID): Future[Option[Equipo]]

  /** Obtener todos los equipos que tienen un nombre en particular. */
  def findByNombre(nombre: String): Future[Seq[Equipo]]

  /** Guardar el equipo. */
  def save(equipo: Equipo, manager: User): Future[Equipo]

  /** Obtener los jugadores de un equipo. */
  def jugadores(equipo: Equipo): Future[Seq[User]]
}
