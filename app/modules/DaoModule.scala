package modules

import com.google.inject.AbstractModule
import dao.TorneoDao
import dao.slick.TorneoDaoSlick

class DaoModule extends AbstractModule {
  def configure() = {
    bind(classOf[TorneoDao])
      .to(classOf[TorneoDaoSlick])
      .asEagerSingleton()
  }
}