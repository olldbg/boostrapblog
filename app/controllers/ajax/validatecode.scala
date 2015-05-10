package controllers.ajax

import controllers.utils.EncryptionUtil._
import play.api._
import play.api.mvc._

/**
 * Created by arch on 5/9/15.
 */
object validatecode extends Controller {
  def validate = Action { implicit request =>
    //print(request.queryString.get("code").get.mkString)
    var inputcode = request.queryString.get("code").get.mkString
    var gencode = request.session.get("MYCAPTCHA").get
    //print(gencode+"\n")
    inputcode = getHash(getHash(inputcode.toLowerCase,"sha")+"scala","md5")
    //print(inputcode+"\n")
    if(inputcode.equals(gencode)) {
      //print("登陆成功")
      Ok("true")
    } else
      Ok("false")
  }
}
