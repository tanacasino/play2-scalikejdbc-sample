package controllers

import play.api._
import play.api.mvc._
import scalikejdbc._

class IndexController extends Controller {

  implicit val session = AutoSession

  def index() = Action {
    Ok("OK")
  }

}
