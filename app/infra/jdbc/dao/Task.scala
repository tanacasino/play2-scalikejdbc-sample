package infra.jdbc.dao

import scalikejdbc._
import java.time.{ ZonedDateTime }
import scalikejdbc.jsr310._

case class Task(
  taskId: Long,
  title: String,
  content: Option[String] = None,
  userId: Long,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime
) {

  def save()(implicit session: DBSession = Task.autoSession): Task = Task.save(this)(session)

  def destroy()(implicit session: DBSession = Task.autoSession): Unit = Task.destroy(this)(session)

}

object Task extends SQLSyntaxSupport[Task] {

  override val tableName = "Task"

  override val columns = Seq("task_id", "title", "content", "user_id", "created_at", "updated_at")

  def apply(t: SyntaxProvider[Task])(rs: WrappedResultSet): Task = apply(t.resultName)(rs)
  def apply(t: ResultName[Task])(rs: WrappedResultSet): Task = new Task(
    taskId = rs.get(t.taskId),
    title = rs.get(t.title),
    content = rs.get(t.content),
    userId = rs.get(t.userId),
    createdAt = rs.get(t.createdAt),
    updatedAt = rs.get(t.updatedAt)
  )

  val t = Task.syntax("t")

  override val autoSession = AutoSession

  def find(taskId: Long)(implicit session: DBSession = autoSession): Option[Task] = {
    withSQL {
      select.from(Task as t).where.eq(t.taskId, taskId)
    }.map(Task(t.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Task] = {
    withSQL(select.from(Task as t)).map(Task(t.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Task as t)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Task] = {
    withSQL {
      select.from(Task as t).where.append(where)
    }.map(Task(t.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Task] = {
    withSQL {
      select.from(Task as t).where.append(where)
    }.map(Task(t.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Task as t).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    taskId: Long,
    title: String,
    content: Option[String] = None,
    userId: Long,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime
  )(implicit session: DBSession = autoSession): Task = {
    withSQL {
      insert.into(Task).namedValues(
        column.taskId -> taskId,
        column.title -> title,
        column.content -> content,
        column.userId -> userId,
        column.createdAt -> createdAt,
        column.updatedAt -> updatedAt
      )
    }.update.apply()

    Task(
      taskId = taskId,
      title = title,
      content = content,
      userId = userId,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(entities: Seq[Task])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'taskId -> entity.taskId,
        'title -> entity.title,
        'content -> entity.content,
        'userId -> entity.userId,
        'createdAt -> entity.createdAt,
        'updatedAt -> entity.updatedAt
      ))
    SQL("""insert into Task(
        task_id,
        title,
        content,
        user_id,
        created_at,
        updated_at
      ) values (
        {taskId},
        {title},
        {content},
        {userId},
        {createdAt},
        {updatedAt}
      )""").batchByName(params: _*).apply()
  }

  def save(entity: Task)(implicit session: DBSession = autoSession): Task = {
    withSQL {
      update(Task).set(
        column.taskId -> entity.taskId,
        column.title -> entity.title,
        column.content -> entity.content,
        column.userId -> entity.userId,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.taskId, entity.taskId)
    }.update.apply()
    entity
  }

  def destroy(entity: Task)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(Task).where.eq(column.taskId, entity.taskId) }.update.apply()
  }

}
