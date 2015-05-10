package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import utils.EncryptionUtil._
import models._;

/**
 * Created by arch on 5/8/15.
 */
object Authorize extends Controller{

  def login = Action { implicit request =>

    val loginForm = Form(
      tuple(
        "username" -> text,
        "password" -> text,
        "validatecode" -> text
      )
    )

    val (username, password,validatecode) = loginForm.bindFromRequest.get
    var gencode = request.session.get("MYCAPTCHA").get
    //print(gencode+"\n")
    var inputcode = getHash(getHash(validatecode.toLowerCase,"sha")+"scala","md5")
    //print(inputcode+"\n")
    var message = ""
    var truepassword = user.getpasswordbyname(username)
    print(truepassword)
    if(inputcode.equals(gencode)==false) {
      //var u = new user(username, password)
      //u.save
      message = "验证码错误"
    }else if(truepassword.isInstanceOf[String]==false) {
      message = "用户名错误"
    }else if(truepassword.equals(password)==false) {
      message = "密码错误"
    }else{
      message = "ok"
    }
    Ok(message)
  }

  def regist = Action { implicit request =>
    val loginForm = Form(
      tuple(
        "username" -> text,
        "password" -> text,
        "confirmpassword" -> text,
        "validatecode" -> text
      )
    )
    val (username, password ,confirmpassword ,validatecode) = loginForm.bindFromRequest.get
    //print("//"+username+":"+password+":"+confirmpassword+":"+validatecode)
    val gencode = request.session.get("MYCAPTCHA").get

    val inputcode = getHash(getHash(validatecode.toLowerCase,"sha")+"scala","md5")
    var message = ""
    if(inputcode.equals(gencode)==false) {
      //var u = new user(username, password)
      //u.save
      message = "验证码错误"
    }else if(user.isuserexist(username)){
      message = "用户名已被注册"
    }else if(username.length<6||username.length>10){
      message = "用户名长度(6-10位)"
    }else if(password.equals(confirmpassword)==false){
      message = "两次输入的密码不一样"
    }else{
      var u = new user(username, password)
      u.save
      message = "ok"
    }
    Ok(message).withSession(
      "MYCAPTCHA" -> "empty")
  }
}
