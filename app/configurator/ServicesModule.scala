package configurator

import models.services.{EquipoService, EquipoServiceImpl}
import models.daos.EquipoDAO
import models.daos.slick.EquipoDaoSlick

/** Configuration for the injector */
class ServicesModule extends com.google.inject.AbstractModule with net.codingwell.scalaguice.ScalaModule {
    
    def configure(): Unit = {
        bind[EquipoService].to[EquipoServiceImpl]
        bind[EquipoDAO].to[EquipoDaoSlick]
    }
}