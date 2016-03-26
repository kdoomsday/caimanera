package dao

import scala.concurrent.Future
import models.Torneo
import java.util.UUID
import models.Equipo
import models.Partido

/**
 * Dao de obtenci&oacute;n de los torneos.
 */
trait TorneoDao {

  /** Todos los torneos de un usuario */
  def byUser(id: UUID): Future[Seq[Torneo]]
  
  /** Agregar un torneo. Devuelve el numero de equipos... */
  def add(t: Torneo, es: Seq[Equipo]): Future[Option[Int]]
  
  /**
   * Modificar un torneo. Busca por id y asigna los demas valores.
   * @return Si existia el torneo y fue exitosa la operacion.
   */
  def edit(idtorneo: Long, nuevoNombre: String): Future[Boolean]
  
  /** Eliminar un torneo segun su id
    * @returns Si fue exitoso.
    */
  def eliminar(id: Long): Future[Boolean]
  
  /** Eliminar un equipo segun su id. Devuelve si fue exitoso */
  def eliminarEquipo(id: Long): Future[Boolean]
  
  /** Detalles de un torneo segun su id */
  def details(id: Long): Future[Option[(Torneo, Seq[Equipo], Seq[Partido])]]
  
  /** Actualizar un equipo. Devuelve si existia el equipo y fue exitosa la actualizacion */
  def actualizarEquipo(id: Long, nombre: String): Future[Boolean]
  
  /** Obtener un equipo por su id */
  def equipoById(id: Long): Future[Option[Equipo]]
  
  /** Agregar un equipo a un torneo. Devuelve si la operacion fue exitosa */
  def addEquipo(nombre: String, idtorneo: Long): Future[Boolean]
  
  /** Todos los equipos registrados en un torneo */
  def equiposTorneo(idtorneo: Long): Future[Seq[Equipo]]
  
  def partidosTorneo(idtorneo: Long): Future[Seq[Partido]]
  
  /** Agregar un partido */
  def agregarPartido(p: Partido): Future[Boolean]
}