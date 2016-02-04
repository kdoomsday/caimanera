package controllers

import services.user.AuthenticationEnvironment
import play.api.i18n.MessagesApi
import play.api.db.slick.HasDatabaseConfig
import slick.driver.JdbcProfile
import play.api.db.slick.DatabaseConfigProvider
import play.api.Play
import dao.slick.TorneoDaoSlick

@javax.inject.Singleton
class TorneoController @javax.inject.Inject() (
    override val messagesApi: MessagesApi,
    override val env: AuthenticationEnvironment)
    extends BaseController
{
  private val torneoDao = new TorneoDaoSlick()

  implicit val execContext = env.executionContext

  def showTorneos = withSession { implicit request =>
    torneoDao.first(10).map { s => Ok(views.html.showTorneos(s)) }
  }
}