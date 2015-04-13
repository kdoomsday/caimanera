package models

import org.joda.time.DateTime

import java.util.UUID

case class Partido(
    id: UUID,
    casa: Equipo,
    visitante: Equipo,
    golesCasa: Int,
    golesVisitante: Int,
    fecha: DateTime)