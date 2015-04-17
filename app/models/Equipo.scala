package models

import java.util.UUID
import org.joda.time.DateTime

/**
 * User: Eduardo Barrientos
 * Date: 8/30/14
 * Time: 10:10 PM
 */
case class Equipo (
  equipoID: UUID,
  nombre: String,
  fechaCreacion: DateTime
)
