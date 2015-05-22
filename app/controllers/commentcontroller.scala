package controllers

import java.util.Date

import models.comment
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

/**
 * Created by arch on 5/19/15.
 */
object commentcontroller extends Controller{
  def postcomment = Action { implicit request =>
    val commentForm = Form(
      tuple(
        "author" -> text,
        "time" -> text,
        "comment" -> text
      )
    )
    var (author, time, comment) = commentForm.bindFromRequest.get
    //println(author+"||"+time+"||"+comment)
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
    var now = new Date
    var c = new comment(time+"||"+author,username,now.getTime.toString,comment)
    c.save
    Ok(c.getformatetime+"##"+c.commenttime)
  }

  def getcommentbyblogid = Action{ implicit request =>
    val blogidForm = Form(
      tuple(
        "blogid" -> text,
        "author" -> text
      )
    )
    var (blogid,author) = blogidForm.bindFromRequest.get

    //println(blogid+"||"+author)
    var username = "empty"
    try {
      username = request.session.get("user").get
    } catch {
      case _: Exception => Unit
    }

    var cs = comment.getcommentbyblogid(blogid)
    var result = new StringBuilder
    var flag = false
    for(i <- 0 until cs.size())
    {
      var c = cs.get(i)
      var tmp = ""
      if(username.equals(author)||username.equals(c.commentuser)) {
        tmp = c.commentuser + "##" + c.comment + "##" + c.commenttime + "##" + c.getformatetime + "##" + "true"
      }else{
        tmp = c.commentuser + "##" + c.comment + "##" + c.commenttime + "##" + c.getformatetime + "##" + "false"
      }
      if(flag == false)
      {
        result.append(tmp)
        flag = true
      }else{
        result.append("%%"+tmp)
      }
    }
    Ok(result.toString)
  }

  def deletecomment = Action { implicit request =>
    val deletecommentForm = Form(
      tuple(
        "blogid" -> text,
        "commenttime" -> text
      )
    )
    var (blogid,commenttime) = deletecommentForm.bindFromRequest.get
    comment.deletecomment(blogid,commenttime)
    Ok("")
  }
}
