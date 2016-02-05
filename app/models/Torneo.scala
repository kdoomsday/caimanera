package models

import java.util.UUID

/** Torneo que se puede jugar. Posiblemente tengo que extender los datos. */
case class Torneo(id: Long, nombre: String, idcreador: UUID)