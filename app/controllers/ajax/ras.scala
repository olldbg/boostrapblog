package controllers.ajax

import controllers.utils.RSAUtils
import org.apache.commons.codec.binary.Hex
import play.api.mvc._

/**
 * Created by arch on 5/10/15.
 */
object ras extends Controller {
  def getpublickey = Action {
    var publicKey = RSAUtils.getDefaultPublicKey()
    Ok(new String(Hex.encodeHex(publicKey.getModulus().toByteArray()))+"||"+new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray())))
  }

}
