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
  
  /** Agregar un torneo
    * @returns Filas agregadas(1)
    */
  def add(t: Torneo): Future[Int]
  
  /** Eliminar un torneo segun su id
    * @returns Filas eliminadas. Deberia ser cero (si no existe) o uno.
    */
  def eliminar(id: Long): Future[Int]
}