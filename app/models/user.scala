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
  var session = ""

  def save = {
    DB.withConnection { implicit c =>
      val id: Option[Long] = SQL("insert into user values ({username}, {password},{sex},{email},{birthday},{phonenumber},{headphoto},{sign},{session})")
        .on('username -> username, 'password -> password, 'sex -> sex, 'email -> email, 'birthday -> birthday, 'phonenumber -> phonenumber, 'headphoto -> headphoto, 'sign -> sign, 'session -> session).executeInsert()
    }
  }

  def update = {
    DB.withConnection { implicit c =>
      val result: Int = SQL("update user set password={password}, sex={sex}, email={email}, birthday={birthday}, phonenumber={phonenumber}, headphoto={headphoto}, sign={sign}, session={session} where username = {username}")
        .on('username -> username, 'password -> password, 'sex -> sex, 'email -> email, 'birthday -> birthday, 'phonenumber -> phonenumber, 'headphoto -> headphoto, 'sign -> sign, 'session -> session).executeUpdate()
    }
  }

  def changeusername(newname: String) = {
    DB.withConnection { implicit c =>
      try {
        val result: Int = SQL("update user set username={newname} where username = {username}")
          .on('username -> username, 'newname -> newname).executeUpdate()
        true
      } catch {
        case _: Exception => false
      }
    }
  }

  def getrecentblog() ={
    DB.withConnection { implicit c =>
      val result = SQL(
        """
      select * from blogs where author = {username} order by publishtime desc
        """
      ).on('username -> username).apply().toList
      if (result.length != 0) {
        val head = result(0)
        var b = new blogs(head[String]("author"),head[String]("publishtime"))
        b.title = head[String]("title")
        b.scope = head[String]("scope")
        b
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
        u.session = head[String]("session")
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

  def deleteuserbyname(name: String) = {
    DB.withConnection { implicit c =>
      val result: Int = SQL("delete from user where username = {username}").on('username -> name).executeUpdate()
    }
  }

  def isuserexist(name: String):Boolean = {
    DB.withConnection { implicit c =>
      val firstRow = SQL("Select count(*) as c from user where username = {username}").on('username -> name).apply().head
      val count = firstRow[Long]("c")
      //print(count)
      if(count==0)
        false
      else
        true
    }
  }

  def setsession(username: String,session: String) = {
    var u = user.getuserbyname(username)
    if(u.isInstanceOf[user])
    {
      var u1 = u.asInstanceOf[user]
      u1.session = session
      u1.update
      true
    }else
    {
      false
    }
  }

  def getsession(username: String) = {
    var u = user.getuserbyname(username)
    if(u.isInstanceOf[user])
    {
      var u1 = u.asInstanceOf[user]
      u1.session
    }else
    {
      "user not found"
    }
  }

}
