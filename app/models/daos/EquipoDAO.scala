package models.daos

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
  def find(id: String): Future[Option[Equipo]]

  /** Obtener todos los equipos que tienen un nombre en particular. */
  def find(nombre: String): Future[Seq[Equipo]]

  /** Guardar el equipo. */
  def save(equipo: Equipo): Future[Equipo]

  /** Obtener los jugadores de un equipo. */
  def jugadores(equipo: Equipo): Future[Seq[User]]
}
