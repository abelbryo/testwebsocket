package controllers

import scala.concurrent._
import ExecutionContext.Implicits.global

import play.api._
import play.api.mvc._

import play.api.libs.iteratee._


object Application extends Controller {

  def index = WebSocket.using[String]{ request =>
    val in = Iteratee.foreach[String](println).map { msg =>
      println(msg)
     }
    val out = Enumerator("Hello") 
    (in, out)
  }

  def test = Action { request =>
    Ok(views.html.index("Hello World"))
  }

}
