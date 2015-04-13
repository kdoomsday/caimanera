package models.daos.slick

import play.api.db.slick.Config.driver.simple._

object DBTableDefinitions {

  case class DBUser (
    userID: String,
    firstName: Option[String],
    lastName: Option[String],
    fullName: Option[String],
    email: Option[String],
    avatarURL: Option[String]
  )

  class Users(tag: Tag) extends Table[DBUser](tag, "user") {
    def id = column[String]("userID", O.PrimaryKey)
    def firstName = column[Option[String]]("firstName")
    def lastName = column[Option[String]]("lastName")
    def fullName = column[Option[String]]("fullName")
    def email = column[Option[String]]("email")
    def avatarURL = column[Option[String]]("avatarURL")
    def * = (id, firstName, lastName, fullName, email, avatarURL) <> (DBUser.tupled, DBUser.unapply)
  }

  case class DBLoginInfo (
    id: Option[Long],
    providerID: String,
    providerKey: String
  )

  class LoginInfos(tag: Tag) extends Table[DBLoginInfo](tag, "logininfo") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def providerID = column[String]("providerID")
    def providerKey = column[String]("providerKey")
    def * = (id.?, providerID, providerKey) <> (DBLoginInfo.tupled, DBLoginInfo.unapply)
  }

  case class DBUserLoginInfo (
    userID: String,
    loginInfoId: Long
  )

  class UserLoginInfos(tag: Tag) extends Table[DBUserLoginInfo](tag, "userlogininfo") {
    def userID = column[String]("userID", O.NotNull)
    def loginInfoId = column[Long]("loginInfoId", O.NotNull)
    def * = (userID, loginInfoId) <> (DBUserLoginInfo.tupled, DBUserLoginInfo.unapply)
  }

  case class DBPasswordInfo (
    hasher: String,
    password: String,
    salt: Option[String],
    loginInfoId: Long
  )

  class PasswordInfos(tag: Tag) extends Table[DBPasswordInfo](tag, "passwordinfo") {
    def hasher = column[String]("hasher")
    def password = column[String]("password")
    def salt = column[Option[String]]("salt")
    def loginInfoId = column[Long]("loginInfoId")
    def * = (hasher, password, salt, loginInfoId) <> (DBPasswordInfo.tupled, DBPasswordInfo.unapply)
  }

  case class DBOAuth1Info (
    id: Option[Long],
    token: String,
    secret: String,
    loginInfoId: Long
  )

  class OAuth1Infos(tag: Tag) extends Table[DBOAuth1Info](tag, "oauth1info") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def token = column[String]("token")
    def secret = column[String]("secret")
    def loginInfoId = column[Long]("loginInfoId")
    def * = (id.?, token, secret, loginInfoId) <> (DBOAuth1Info.tupled, DBOAuth1Info.unapply)
  }

  case class DBOAuth2Info (
    id: Option[Long],
    accessToken: String,
    tokenType: Option[String],
    expiresIn: Option[Int],
    refreshToken: Option[String],
    loginInfoId: Long
  )

  class OAuth2Infos(tag: Tag) extends Table[DBOAuth2Info](tag, "oauth2info") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def accessToken = column[String]("accesstoken")
    def tokenType = column[Option[String]]("tokentype")
    def expiresIn = column[Option[Int]]("expiresin")
    def refreshToken = column[Option[String]]("refreshtoken")
    def loginInfoId = column[Long]("logininfoid")
    def * = (id.?, accessToken, tokenType, expiresIn, refreshToken, loginInfoId) <> (DBOAuth2Info.tupled, DBOAuth2Info.unapply)
  }


  case class DBEquipo(nombre: String, id: String)
  class Equipos(tag: Tag) extends Table[DBEquipo](tag, "Equipos") {
    def id = column[String]("id", O.PrimaryKey)
    def nombre = column[String]("nombre", O.NotNull)
    def * = (nombre, id) <> (DBEquipo.tupled, DBEquipo.unapply)
  }
  
  
  case class DBPartido(id: String, idCasa: String, idVisitante: String, golCasa: Int, golVisitante: Int)
  class Partidos(tag: Tag) extends Table[DBPartido](tag, "Partidos") {
      def id = column[String]("id", O.PrimaryKey)
      def idCasa = column[String]("idCasa")
      def idVisitante = column[String]("idVistante")
      
      def golCasa = column[Int]("golesCasa", O.Default(0))
      def golVisitante = column[Int]("golesVisitante", O.Default(0))
      
      def * = (id, idCasa, idVisitante, golCasa, golVisitante) <> (DBPartido.tupled, DBPartido.unapply)
      
      
      def casa = foreignKey("FK_ID_CASA", idCasa, slickEquipos)(_.id)
      def visitante = foreignKey("FK_ID_VISITANTE", idVisitante, slickEquipos)(_.id)
  }


  class JugadorEquipo(tag: Tag) extends Table[(String, String, Boolean)](tag, "JugadorEquipo") {
    def idequipo = column[String]("idequipo", O.NotNull)
    def iduser = column[String]("iduser", O.NotNull)
    def esManager = column[Boolean]("esManager", O.NotNull, O.Default(false))
    def pk = primaryKey("pk_jugadorEquipo", (idequipo, iduser))
    def * = (idequipo, iduser, esManager)
  }

  val slickUsers = TableQuery[Users]
  val slickLoginInfos = TableQuery[LoginInfos]
  val slickUserLoginInfos = TableQuery[UserLoginInfos]
  val slickPasswordInfos = TableQuery[PasswordInfos]
  val slickOAuth1Infos = TableQuery[OAuth1Infos]
  val slickOAuth2Infos = TableQuery[OAuth2Infos]
  val slickEquipos = TableQuery[Equipos]
  val slickJugadorEquipo = TableQuery[JugadorEquipo]
  val slickPartidos = TableQuery[Partidos]
}
