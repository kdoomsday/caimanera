package models.services

import scala.concurrent.Future
import javax.inject.Inject

import models.Equipo
import models.daos.EquipoDAO

/**
 * Implementaci&oacute;n del servicio EquipoService.
 */
class EquipoServiceImpl @Inject() (dao: EquipoDAO) extends EquipoService {
    
    override def save(equipo: Equipo): Future[Equipo] =
        dao.save(equipo)
}