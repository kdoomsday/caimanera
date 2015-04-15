package models.services

import scala.concurrent.Future
import javax.inject.Inject

import models.{User, Equipo}
import models.daos.EquipoDAO

/**
 * Implementaci&oacute;n del servicio EquipoService.
 */
class EquipoServiceImpl @Inject() (dao: EquipoDAO) extends EquipoService {
    
    override def save(equipo: Equipo, manager: User): Future[Equipo] = {
        println(s"Guardando equipo ${equipo.nombre} de ${manager.fullName}")
        dao.save(equipo, manager)
    }
}