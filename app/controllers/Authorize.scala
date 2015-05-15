package controllers

import java.util.Random

import controllers.utils.RSAUtils
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

    val (username, passwordencrypt,validatecode) = loginForm.bindFromRequest.get

    var password = RSAUtils.decryptStringByJs(passwordencrypt)

    var gencode = request.session.get("MYCAPTCHA").get
    //print(gencode+"\n")
    var inputcode = getHash(getHash(validatecode.toLowerCase,"sha")+"scala","md5")
    //print(inputcode+"\n")
    var message = ""
    var sessionid:Long = 0
    var truepassword = user.getpasswordbyname(username)
    //print(truepassword)
    if(inputcode.equals(gencode)==false) {
      //var u = new user(username, password)
      //u.save
      message = "验证码错误"
    }else if(truepassword.isInstanceOf[String]==false||truepassword.equals(password)==false) {
      message = "用户名或密码错误"
    }else{
      //print("登陆成功")
      var random = new Random()
      sessionid = random.nextLong()
      user.setsession(username,sessionid+"")
      message = "ok"
    }
    Ok(message).withSession(
      "MYCAPTCHA" -> "empty", "user" -> username,"usersession" ->(sessionid+""),"alertmessage" -> "login")
  }

  def logout = Action { implicit request =>
    Ok("").withSession("usersession" -> "empty")
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
    val (username, passwordencrypt ,confirmpasswordencrypt ,validatecode) = loginForm.bindFromRequest.get
    //print("//"+username+":"+passwordencrypt+":"+confirmpasswordencrypt+":"+validatecode)
    var password = RSAUtils.decryptStringByJs(passwordencrypt)
    var confirmpassword = RSAUtils.decryptStringByJs(confirmpasswordencrypt)
    //print("//"+username+":"+password+":"+confirmpassword+":"+validatecode)
    val gencode = request.session.get("MYCAPTCHA").get

    val inputcode = getHash(getHash(validatecode.toLowerCase,"sha")+"scala","md5")
    var message = ""
    var sessionid:Long = 0
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
      var random = new Random()
      sessionid = random.nextLong()
      user.setsession(username,sessionid+"")
      message = "ok"
    }
    Ok(message).withSession(
      "MYCAPTCHA" -> "empty", "user" -> username,"usersession" ->(sessionid+""),"alertmessage" -> "regist")
  }
}
