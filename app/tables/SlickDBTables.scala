package tables

import slick.driver.JdbcProfile
import models.{ Torneo, Equipo, Partido }
import java.util.UUID
import java.sql.Timestamp
import com.github.nscala_time.time.Imports._
import org.joda.time.DateTime

trait SlickDBTables {
  protected val driver: JdbcProfile
  import driver.api._
  
  
  def datetime2Timestamp(d: DateTime): Timestamp = new Timestamp(d.getMillis)
  
  
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
    
    foreignKey("fk_torneo", idtorneo, torneos)(_.id, onUpdate=ForeignKeyAction.Cascade, onDelete=ForeignKeyAction.Cascade)
  }
  
  
  class Partidos(tag: Tag) extends Table[Partido](tag, "partido") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def idtorneo = column[Long]("idtorneo")
    def idcasa = column[Long]("idcasa")
    def idvisitante = column[Long]("idvisitante")
    def fecha = column[Timestamp]("fecha")
    def scoreCasa = column[Int]("scorecasa", O.Default(0))
    def scoreVisitante = column[Int]("scorevisitante", O.Default(0))
    
    import ForeignKeyAction.Cascade
    foreignKey("fk_partido_torneo", idtorneo, torneos)(_.id, onDelete=Cascade, onUpdate=Cascade)
    foreignKey("fk_casa_equipo", idcasa, equipos)(_.id, onDelete=Cascade, onUpdate=Cascade)
    foreignKey("fk_visitante_equipo", idvisitante, equipos)(_.id, onDelete=Cascade, onUpdate=Cascade)
    
    def * = (id, idtorneo, idcasa, scoreCasa, idvisitante, scoreVisitante, fecha).shaped <> (
      { case (id, idtorneo, idcasa, scoreCasa, idvisitante, scoreVisitante, fecha) ⇒
          Partido(id, idtorneo, idcasa, scoreCasa, idvisitante, scoreVisitante, new DateTime(fecha))
      },
      { p: Partido ⇒ Some((-1L, p.idTorneo, p.idcasa, p.scoreCasa, p.idvisitante, p.scoreVisitante, datetime2Timestamp(p.fecha))) }
    )
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
  
  
  val torneos =  TableQuery[Torneos]
  val users   =  TableQuery[Users]
  val equipos =  TableQuery[Equipos]
  val partidos = TableQuery[Partidos]
}
