package controllers

import java.io.{FileReader, BufferedReader, File}
import java.util
import java.util.Random

import play.api._
import play.api.mvc._

import anorm._
import play.api.db.DB
import play.api.Play.current

import models._
import utils.RSAUtils._

object Application extends Controller {

  def index = Action { implicit request =>
    var username = "empty"
    var sessionid = "empty"
    var alertmessage = "empty"
    try {
      username = request.session.get("user").get
      sessionid = request.session.get("usersession").get
      alertmessage = request.session.get("alertmessage").get
    } catch {
      case _: Exception => Unit
    }
    //



    //print(username+"::"+sessionid+"\n")
    if (username.equals("empty") == false&&username.equals("empty") == false&&sessionid.equals(user.getsession(username))) {
      var alert = ""
      if(alertmessage.equals("login"))
      {
        alert = "登陆成功"
      }else if(alertmessage.equals("regist")){
        alert = "注册成功"
      }else if(alertmessage.equals("post")){
        alert = "发布成功"
      }

      var bloglist = blogs.getlist(5,0)
//      var i = 0
//      while(i<list.size())
//      {
//        //print(list.get(i).publishtime+"\n")
//        print(list.get(i).getarticle)
//        i += 1
//      }
//      for(i <- 0 until bloglist.size())
//        print(bloglist.get(i).blogid+"\n")

      Ok(views.html.logined("",alert,bloglist)).withSession("user" -> username,"usersession" ->(sessionid+""),"alertmessage" -> "")
    } else {
      Ok(views.html.index(""))
    }
  }

  def photo = Action { implicit request =>
    Ok("")
  }
}