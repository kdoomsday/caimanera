package dao

import scala.concurrent.Future
import models.Torneo
import java.util.UUID
import models.Equipo

/**
 * Dao de obtenci&oacute;n de los torneos.
 */
trait TorneoDao {

  /** Todos los torneos de un usuario */
  def byUser(id: UUID): Future[Seq[Torneo]]
  
  /** Agregar un torneo. Devuelve el numero de equipos... */
  def add(t: Torneo, es: Seq[Equipo]): Future[Option[Int]]
  
  /** Eliminar un torneo segun su id
    * @returns Filas eliminadas. Deberia ser cero (si no existe) o uno.
    */
  def eliminar(id: Long): Future[Int]
  
  /** Detalles de un torneo segun su id */
  def details(id: Long): Future[Option[(Torneo, Seq[Equipo])]]
}