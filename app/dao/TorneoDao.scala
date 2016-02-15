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
  
  /** Eliminar un equipo segun su id. Devuelve si fue exitoso */
  def eliminarEquipo(id: Long): Future[Boolean]
  
  /** Detalles de un torneo segun su id */
  def details(id: Long): Future[Option[(Torneo, Seq[Equipo])]]
  
  /** Actualizar un equipo. Devuelve si existia el equipo y fue exitosa la actualizacion */
  def actualizarEquipo(id: Long, nombre: String): Future[Boolean]
  
  /** Obtener un equipo por su id */
  def equipoById(id: Long): Future[Option[Equipo]]
  
  /** Agregar un equipo a un torneo. Devuelve si la operacion fue exitosa */
  def addEquipo(nombre: String, idtorneo: Long): Future[Boolean]
}