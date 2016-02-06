package dao

import scala.concurrent.Future
import models.Torneo
import java.util.UUID

/**
 * Dao de obtenci&oacute;n de los torneos.
 */
trait TorneoDao {

  /** Los primeros n torneos registrados. */
  def first(n: Int): Future[Seq[Torneo]]
  
  /** Todos los torneos de un usuario */
  def byUser(id: UUID): Future[Seq[Torneo]]
  
  /** Agregar un torneo */
  def add(t: Torneo): Future[Unit]
}