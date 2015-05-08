package controllers

import play.api._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import com.octo.captcha._

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

    val (user, password,validatecode) = loginForm.bindFromRequest.get
    print(validatecode)
    print("登陆成功")
    Ok(views.html.index("Your new application is ready."))
  }

}
