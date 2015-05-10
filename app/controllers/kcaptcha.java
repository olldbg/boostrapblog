package controllers;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import controllers.utils.EncryptionUtil;
import play.Logger;
import play.mvc.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

/**
 * Created by arch on 5/8/15.
 */
public class kcaptcha extends Controller {
    public static Result captcha(){
        DefaultKaptcha captchaPro=new DefaultKaptcha();
        Properties p = new Properties();
        p.setProperty("kaptcha.background.clear.from","white");
        p.setProperty("kaptcha.image.width","150");
        p.setProperty("kaptcha.image.height","50");
        p.setProperty("kaptcha.border","no");
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        p.setProperty("kaptcha.textproducer.font.color",r+","+g+","+b);
        p.setProperty("kaptcha.textproducer.font.size","30");
        p.setProperty("kaptcha.noise.color", r+","+g+","+b);
        captchaPro.setConfig(new Config(p));
        String text=captchaPro.createText();
        session().put("MYCAPTCHA", EncryptionUtil.getHash(EncryptionUtil.getHash(text.toLowerCase(),"sha")+"scala","md5"));
        //Logger.debug("Captcha:" + text);//U can put the text in cache.
        BufferedImage img=captchaPro.createImage(text);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            ImageIO.write(img, "jpg", baos);
            baos.flush();
        }catch(IOException e){
            Logger.debug(e.getMessage());
        }
        return ok(baos.toByteArray()).as("image/jpg");
    }
}


