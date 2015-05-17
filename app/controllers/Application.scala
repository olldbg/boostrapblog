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
    var devide = 5
    var username = "empty"
    var sessionid = "empty"
    var alertmessage = "empty"
    var page = 1
    try {
      username = request.session.get("user").get
      sessionid = request.session.get("usersession").get
      alertmessage = request.session.get("alertmessage").get

    } catch {
      case _: Exception => Unit
    }
    //
    try{
      page = java.lang.Integer.parseInt(request.queryString.get("page").get.mkString)
      if(page<1)
        page=1
    }catch {
      case _: Exception => Unit
    }
    val count = blogs.getcount()
    val maxpage = (count.asInstanceOf[Int]-1)/devide + 1
    if((page-1)*devide>=count)
    {
      page = maxpage
    }
    //println(page+"||"+blogs.getcount())
    //print(username+"::"+sessionid+"\n")
    if (username.equals("empty") == false&&sessionid.equals("empty") == false&&sessionid.equals(user.getsession(username))) {
      var alert = ""
      if(alertmessage.equals("login"))
      {
        alert = "登陆成功"
      }else if(alertmessage.equals("regist")){
        alert = "注册成功"
      }else if(alertmessage.equals("post")){
        alert = "发布成功"
      }else if(alertmessage.equals("ok")){
        alert = "操作完成"
      }

      var bloglist = blogs.getlist(5,page-1)
//      var i = 0
//      while(i<list.size())
//      {
//        //print(list.get(i).publishtime+"\n")
//        print(list.get(i).getarticle)
//        i += 1
//      }
//      for(i <- 0 until bloglist.size())
//        print(bloglist.get(i).blogid+"\n")
      var u = user.getuserbyname(username)
      if(u.isInstanceOf[Unit]){
        Ok(views.html.index(""))
      }else {
        if(u.asInstanceOf[user].sign.equals(""))
          u.asInstanceOf[user].sign="（空）"
        Ok(views.html.logined("", alert, bloglist, u.asInstanceOf[user], page, maxpage)).withSession("user" -> username, "usersession" -> (sessionid + ""), "alertmessage" -> "")
      }
    } else {
      Ok(views.html.index(""))
    }
  }

  def photo = Action { implicit request =>
    Ok("")
  }
}