package models

import org.joda.time.DateTime
import java.util.UUID

case class Partido(idTorneo: Long, idcasa: Long, idvisitante: Long, fecha: DateTime, scoreCasa: Int, scoreVisitante: Int)