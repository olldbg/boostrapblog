package controllers

import java.io.{FileWriter, File}
import java.util.Date

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import models._

/**
 * Created by arch on 5/12/15.
 */
object blogcontroller extends Controller {
  def postblog = Action { implicit request =>

    val blogForm = Form(
      tuple(
        "title" -> text,
        "article" -> text
      )
    )
    var (title, article) = blogForm.bindFromRequest.get

    if(title.equals("")) {
      Ok("标题为空")
    }
    else {
      if(article.equals(""))
        article="(内容为空)"

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
      if(username.equals("empty")) {
        Ok("出错")
      }else{
        var time = new Date()
        var b = new blogs(username,time.getTime+"")
        b.title = title
        b.save

        var file = new File("articlepool")
        if(file.exists()==false)
        {
          file.mkdirs()
        }
        file = new File("articlepool/"+b.blogid)
        if(file.exists()==false)
        {
          file.createNewFile()
        }
        var fw = new FileWriter(file)
        fw.write(article)
        fw.flush()
        fw.close()

        Ok("ok").withSession("user" -> username,"usersession" ->(sessionid+""),"alertmessage" -> "post")
      }
    }
  }

  def deleteblog = Action { implicit request =>

    val deleteForm = Form(
      tuple(
        "author" -> text,
        "publishtime" -> text
      )
    )
    var (author, publishtime) = deleteForm.bindFromRequest.get
    var username = "empty"
    var sessionid = "empty"
    try {
      username = request.session.get("user").get
      sessionid = request.session.get("usersession").get
    } catch {
      case _: Exception => Unit
    }
    if (sessionid.equals("empty") == false&&sessionid.equals(user.getsession(author))) {
      blogs.deleteblog(author,publishtime)
    }
    Ok("").withSession("user" -> username, "usersession" -> (sessionid + ""), "alertmessage" -> "ok")
  }
}
