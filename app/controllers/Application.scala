package controllers

import play.api._
import play.api.mvc._

import anorm._
import play.api.db.DB
import play.api.Play.current

import models._;

object Application extends Controller {

  def index = Action {

//    DB.withConnection { implicit c =>
//      val id: Option[Long] = SQL("insert into test(id, name) values ({id}, {name})")
//        .on('id -> 2, 'name -> "New Zealand").executeInsert()
//    }
//    var u = new user("name1","password")
//    u.email="123@qq.com"
//    u.birthday="16253123"
//    u.sex=false
//    u.phonenumber="3261873681"
//    u.headphoto="/shax/xbsak"
//    u.sign="cbahcbkjasbckasbjc"
//    u.save
//      val u = user.getuserbyname("name3")
//      if(u.isInstanceOf[user]) {
//        var u1 = u.asInstanceOf[user]
//        u1.password="123"
//        u1.phonenumber="17238127368"
//        u1.birthday="/12/321"
//        u1.sign="haha"
//        u1.update
//        if(u1.changeusername("name1")==false)
//        {
//          print("更改失败")
//        }
//      }
//    user.deleteuserbyname("name3")
//    print(user.getpasswordbyname("name2"))
    Ok(views.html.index("Your new application is ready."))
  }

}