package models

import org.joda.time.DateTime

case class Partido(idcasa: Long, idvisitante: Long, fecha: DateTime, scoreCasa: Int, scoreVisitante: Int)