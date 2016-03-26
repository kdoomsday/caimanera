package models

import org.joda.time.DateTime
import java.util.UUID

case class Partido(
    idTorneo: Long,
    idcasa: Long,
    scoreCasa: Int,
    idvisitante: Long,
    scoreVisitante: Int,
    fecha: DateTime
)