package controllers

import java.lang.management.ManagementFactory

import play.api._
import play.api.libs.concurrent.Promise
import play.api.libs.iteratee._
import play.api.mvc._

import scala.concurrent._
import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

object Application extends Controller {

  def index = WebSocket.using[String] { request =>
    val in = Iteratee.foreach[String](println).map { msg =>
      println(msg)
    }
    val out = Enumerator("Hello")
    (in, out)
  }

  def test = Action { request =>
    Ok(views.html.index("Hello World"))
  }

  def statusPage = Action { implicit request =>
      Ok(views.html.statusPage(request))
  }

  def statusFeed() = WebSocket.using[String] { implicit request =>
    def getLoadAverage = {
      "%1.2f" format ManagementFactory.getOperatingSystemMXBean
        .getSystemLoadAverage()
    }

    val in = Iteratee.ignore[String]
    val out = Enumerator.repeatM {
      Promise.timeout(getLoadAverage, 3 seconds)
    }

    (in, out)
  }

}
