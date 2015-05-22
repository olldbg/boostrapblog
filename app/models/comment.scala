package models

import java.text.SimpleDateFormat
import java.util
import java.util._

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by arch on 5/7/15.
 */
class comment(var blogid: String,var commentuser:String,var commenttime: String,var comment: String) {
  var isreply = false
  var replyuser = ""
  def save = {
    DB.withConnection { implicit c =>
      val id: Option[Long] = SQL("insert into comment (blogid,commentuser,commenttime,comment,isreply,replyuser) values ({blogid}, {commentuser},{commenttime},{comment},{isreply},{replyuser})")
        .on('blogid -> blogid, 'commentuser -> commentuser , 'commenttime -> commenttime, 'comment -> comment, 'isreply -> isreply, 'replyuser -> replyuser).executeInsert()
    }
  }

  def getformatetime = {
    var formater = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
    var time = new Date(java.lang.Long.parseLong(commenttime))
    formater.format(time)
  }
}

object comment{
  def getcommentbyblogid(blogid:String) :ArrayList[comment] = {
    var comments = new ArrayList[comment]()
    DB.withConnection { implicit c =>
      val result = SQL(
        """
      select * from comment where blogid={blogid} order by commenttime asc
        """
      ).on('blogid -> blogid).apply().toList
      val len = result.length
      var i = 0
      while(i < len)
      {
        val row = result(i)
        var c = new comment(row[String]("blogid"),row[String]("commentuser"),row[String]("commenttime"),row[String]("comment"))
        comments.add(c)
        i += 1
      }
      comments
    }
  }

  def deletecomment(blogid: String,commenttime: String) = {
    DB.withConnection { implicit c =>
      val result: Int = SQL("delete from comment where blogid = {blogid} and commenttime = {commenttime}").on('blogid -> blogid,'commenttime -> commenttime).executeUpdate()
    }
  }

}