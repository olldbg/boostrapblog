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

      //test

      //comment.deletecomment("1432174879273||abcdef","1432208618761")
//      var i = 0
//      while(i<list.size())
//      {
//        //print(list.get(i).publishtime+"\n")
//        print(list.get(i).getarticle)
//        i += 1
//      }
//      for(i <- 0 until bloglist.size())
//        print(bloglist.get(i).blogid+"\n")
//      var cs = comment.getcommentbyblogid("1431865495848||123456")
//      println(cs.size())
//      for(i <- 0 until cs.size())
//      {
//         println(cs.get(i).blogid+"##"+cs.get(i).commentuser+"||"+cs.get(i).commenttime+"||"+cs.get(i).comment+"||"+cs.get(i).isreply+"||"+cs.get(i).replyuser)
//      }
      //endtest


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
    Ok(views.html.photo(""))
  }

  def clean = Action { implicit request =>
    var file = new File("articlepool/trash")
    if(file.exists()==false)
    {
      file.mkdirs()
    }
    file = new File("articlepool")
    var files = file.listFiles()
    //printf(files.toString)
    var bs = blogs.getall()
    var blogids = Set("")
    var len = bs.size()
    for(i <- 0 until len)
    {
      blogids+=bs.get(i).blogid
    }
//    for(i <- 0 until len)
//    {
//      println(blogids.get(i))
//    }
    var filenames = Set("")

    for(i <- 0 until files.size)
    {
      if(files(i).isDirectory==false)
      {
        filenames+=files(i).getName
      }
    }
    var result = filenames -- blogids
    for(i <- result)
    {
      var oldfile = new File("articlepool/"+i)
      var newfile = new File("articlepool/trash/"+i)
      oldfile.renameTo(newfile)
      //println(i)
    }
    //println(filenames.size+"##"+blogids.size+"##"+result.size)
    Ok("")
  }

}