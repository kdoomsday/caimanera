package tables

import slick.driver.JdbcProfile
import models.Torneo

trait TorneoTable {
  protected val driver: JdbcProfile
  import driver.api._

  class Torneos(tag: Tag) extends Table[Torneo](tag, "Torneo") {
    def id = column[Long]("id", O.PrimaryKey)
    def nombre = column[String]("nombre")

    def * = (id, nombre) <> (Torneo.tupled, Torneo.unapply)
  }
}