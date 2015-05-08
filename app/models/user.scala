package models

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by arch on 5/7/15.
 */
class user(var username: String, var password: String) {
  var sex = true
  var email = ""
  var birthday = ""
  var phonenumber = ""
  var headphoto = ""
  var sign = ""

  def save = {
    DB.withConnection { implicit c =>
      val id: Option[Long] = SQL("insert into user values ({username}, {password},{sex},{email},{birthday},{phonenumber},{headphoto},{sign})")
        .on('username -> username, 'password -> password, 'sex -> sex, 'email -> email, 'birthday -> birthday, 'phonenumber -> phonenumber, 'headphoto -> headphoto, 'sign -> sign).executeInsert()
    }
  }

  def update = {
    DB.withConnection { implicit c =>
      val result: Int = SQL("update user set password={password}, sex={sex}, email={email}, birthday={birthday}, phonenumber={phonenumber}, headphoto={headphoto}, sign={sign} where username = {username}")
        .on('username -> username, 'password -> password, 'sex -> sex, 'email -> email, 'birthday -> birthday, 'phonenumber -> phonenumber, 'headphoto -> headphoto, 'sign -> sign).executeUpdate()
    }
  }

  def changeusername(newname: String) = {
    DB.withConnection { implicit c =>
      try {
        val result: Int = SQL("update user set username={newname} where username = {username}")
          .on('username -> username, 'newname -> newname).executeUpdate()
        true
      }catch {
        case _: Exception => false
      }
    }
  }
}

object user {
  def getuserbyname(name: String) = {
    var u = new user(name, "")

    DB.withConnection { implicit c =>
      val result = SQL(
        """
      select * from user where username={name}
        """
      ).on('name -> name).apply().toList
      if (result.length != 0) {
        val head = result(0)
        u.password = head[String]("password")
        u.sex = head[Boolean]("sex")
        u.email = head[String]("email")
        u.birthday = head[String]("birthday")
        u.phonenumber = head[String]("phonenumber")
        u.headphoto = head[String]("headphoto")
        u.sign = head[String]("sign")
        u
      } else {
        Unit
      }
    }
  }

  def getpasswordbyname(name: String) = {
    DB.withConnection { implicit c =>
      try {
        val password = SQL(
          """
        select password from user where username={name}
          """
        ).on('name -> name).as(scalar[String].single)
        password
      } catch {
        case _: Exception => Unit
      }
    }
  }
}
