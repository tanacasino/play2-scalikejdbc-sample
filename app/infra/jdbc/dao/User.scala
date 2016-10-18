package infra.jdbc.dao

import scalikejdbc._
import java.time.{LocalDate, ZonedDateTime}
import scalikejdbc.jsr310._

case class User(
  userId: Long,
  name: String,
  password: String,
  birthday: LocalDate,
  registeredAt: ZonedDateTime,
  updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = User.autoSession): User = User.save(this)(session)

  def destroy()(implicit session: DBSession = User.autoSession): Unit = User.destroy(this)(session)

}


object User extends SQLSyntaxSupport[User] {

  override val tableName = "User"

  override val columns = Seq("user_id", "name", "password", "birthday", "registered_at", "updated_at")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User = new User(
    userId = rs.get(u.userId),
    name = rs.get(u.name),
    password = rs.get(u.password),
    birthday = rs.get(u.birthday),
    registeredAt = rs.get(u.registeredAt),
    updatedAt = rs.get(u.updatedAt)
  )

  val u = User.syntax("u")

  override val autoSession = AutoSession

  def find(userId: Long)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.eq(u.userId, userId)
    }.map(User(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[User] = {
    withSQL(select.from(User as u)).map(User(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(User as u)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(User as u).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userId: Long,
    name: String,
    password: String,
    birthday: LocalDate,
    registeredAt: ZonedDateTime,
    updatedAt: ZonedDateTime)(implicit session: DBSession = autoSession): User = {
    withSQL {
      insert.into(User).namedValues(
        column.userId -> userId,
        column.name -> name,
        column.password -> password,
        column.birthday -> birthday,
        column.registeredAt -> registeredAt,
        column.updatedAt -> updatedAt
      )
    }.update.apply()

    User(
      userId = userId,
      name = name,
      password = password,
      birthday = birthday,
      registeredAt = registeredAt,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: Seq[User])(implicit session: DBSession = autoSession): Seq[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'userId -> entity.userId,
        'name -> entity.name,
        'password -> entity.password,
        'birthday -> entity.birthday,
        'registeredAt -> entity.registeredAt,
        'updatedAt -> entity.updatedAt))
        SQL("""insert into User(
        user_id,
        name,
        password,
        birthday,
        registered_at,
        updated_at
      ) values (
        {userId},
        {name},
        {password},
        {birthday},
        {registeredAt},
        {updatedAt}
      )""").batchByName(params: _*).apply()
    }

  def save(entity: User)(implicit session: DBSession = autoSession): User = {
    withSQL {
      update(User).set(
        column.userId -> entity.userId,
        column.name -> entity.name,
        column.password -> entity.password,
        column.birthday -> entity.birthday,
        column.registeredAt -> entity.registeredAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.userId, entity.userId)
    }.update.apply()
    entity
  }

  def destroy(entity: User)(implicit session: DBSession = autoSession): Unit = {
    withSQL { delete.from(User).where.eq(column.userId, entity.userId) }.update.apply()
  }

}
