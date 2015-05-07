package controllers

import play.api._
import play.api.mvc._

import anorm._
import play.api.db.DB
import play.api.Play.current

object Application extends Controller {

  def index = Action {

//    DB.withConnection { implicit c =>
//      val id: Option[Long] = SQL("insert into test(id, name) values ({id}, {name})")
//        .on('id -> 2, 'name -> "New Zealand").executeInsert()
//    }

    Ok(views.html.index("Your new application is ready."))
  }

}