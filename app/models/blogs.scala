package models

import java.text.SimpleDateFormat
import java.util
import java.util._

import anorm._
import play.api.db.DB
import play.api.Play.current

import scala.io.Source

/**
 * Created by arch on 5/7/15.
 */
class blogs(var author:String, var publishtime:String) {
  var blogid = publishtime + "||" + author
  var title = ""
  var scope = "public"

  def save = {
    DB.withConnection { implicit c =>
      val id: Option[Long] = SQL("insert into blogs values ({blogid}, {author},{publishtime},{title},{scope})")
        .on('blogid -> blogid, 'author -> author , 'publishtime -> publishtime, 'title -> title, 'scope -> scope).executeInsert()
    }
  }

  def getarticle:String = {
    var source = Source.fromFile("articlepool/"+blogid)
    source.mkString
  }

  def getformatetime():String = {
    var formater = new SimpleDateFormat("yyyy年MM月dd日")
    var time = new Date(java.lang.Long.parseLong(publishtime))
    formater.format(time)
  }
}

object blogs{

  def getlist(lenth:Int, index:Int) =
  {
    var bs = new ArrayList[blogs]()
    DB.withConnection { implicit c =>
      val result = SQL(
        """
      select * from blogs order by blogid desc
        """
      ).apply().toList
      if (result.length != 0) {
        var i = lenth*index
        var j = 0
        while(i+j<result.length&&j<lenth) {
          val row = result(i+j)
          val author = row[String]("author")
          val publishtime = row[String]("publishtime")
          var b = new blogs(author,publishtime)
          b.title = row[String]("title")
          b.scope = row[String]("scope")
          j += 1
          bs.add(b)
        }
      }
    }
    bs
  }

  def getcount() =
  {
    DB.withConnection { implicit c =>
      val result = SQL(
        """
      select count(*) as c from blogs
        """
      ).apply().toList
      if (result.length != 0) {
        val row = result(0)
        row[Long]("c")
      }else{
        0
      }
    }
  }

  def deleteblog(author:String, publishtime: String) = {
    DB.withConnection { implicit c =>
      var blogid = publishtime + "||" + author
      val result: Int = SQL("delete from blogs where blogid = {blogid}").on('blogid -> blogid).executeUpdate()
    }
  }
}
