package tables

import slick.driver.JdbcProfile
import models.Torneo
import java.util.UUID
import java.sql.Timestamp
import models.Equipo

trait SlickDBTables {
  protected val driver: JdbcProfile
  import driver.api._
  
  class Torneos(tag: Tag) extends Table[Torneo](tag, "torneo") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")
    def idcreador = column[UUID]("idcreador")

    def * = (id, nombre, idcreador) <> (Torneo.tupled, Torneo.unapply)
    
    foreignKey("fk_idcreador", idcreador, users)(_.id, onUpdate=ForeignKeyAction.Restrict)
  }
  
  
  class Equipos(tag: Tag) extends Table[Equipo](tag, "equipo") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def nombre = column[String]("nombre")
    def idtorneo = column[Long]("idtorneo")
    
    def * = (id, nombre, idtorneo) <> (Equipo.tupled, Equipo.unapply)
    
    foreignKey("fk_torneo", idtorneo, torneos)(_.id)
  }
  
  
  /**
   * Usuario de base de datos.
   * NOTA: Mapeado sin alguno de los campos. Si hacen falta se agregan.
   */
  case class SlickUser(id: UUID, username: Option[String], created: Timestamp)
  class Users(tag: Tag) extends Table[SlickUser](tag, "users") {
    def id = column[UUID]("id", O.PrimaryKey)
    def username = column[Option[String]]("username")
    def created = column[Timestamp]("created")
    
    def * = (id, username, created) <> (SlickUser.tupled, SlickUser.unapply)
  }
  
  
  val torneos = TableQuery[Torneos]
  val users   = TableQuery[Users]
  val equipos = TableQuery[Equipos]
}