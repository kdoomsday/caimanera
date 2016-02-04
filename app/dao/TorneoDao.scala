package dao

import scala.concurrent.Future
import models.Torneo

/**
 * Dao de obtenci&oacute;n de los torneos.
 */
trait TorneoDao {

  /** Los primeros n torneos registrados. */
  def first(n: Int): Future[Seq[Torneo]]
}