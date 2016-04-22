package models

import org.joda.time.DateTime

case class Partido(
    id:             Long,
    idTorneo:       Long,
    idcasa:         Long,
    scoreCasa:      Int,
    idvisitante:    Long,
    scoreVisitante: Int,
    fecha:          DateTime
)
