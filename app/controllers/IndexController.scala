package controllers

import play.api._
import play.api.mvc._
import scalikejdbc._

class IndexController extends Controller {

  implicit val session = AutoSession

  def index() = Action {
    val accounts = {
      try sql"select * from accounts".toMap.list.apply()
      catch {
        case e: Exception =>
          sql"create table accounts(name varchar(100) not null)".execute.apply()
          Seq("Alice", "Bob", "Chris").foreach { name =>
            sql"insert into accounts values ($name)".update.apply()
          }
          sql"select * from accounts".toMap.list.apply()
      }
    }
    Ok(accounts.toString)
  }

}
