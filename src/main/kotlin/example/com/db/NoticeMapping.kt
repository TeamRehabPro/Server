package example.com.db

import example.com.noticeModel.Notice
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object NoticeTable : IntIdTable("notice") {
    val employeeId = varchar("employee_id", 50)
    val creationDate = date("creation_date")
    val title = varchar("title", 50)
    val content = varchar("content", 255)
}

class NoticeDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NoticeDAO>(NoticeTable)

    var employeeId by NoticeTable.employeeId
    var creationDate by NoticeTable.creationDate
    var title by NoticeTable.title
    var content by NoticeTable.content
}

suspend fun <T> noticeSuspendTransaction(block: Transaction.() -> T): T =
    runSuspendTransaction(block)

fun noticeDaoToModel(dao: NoticeDAO) = Notice(
    dao.employeeId,
    dao.creationDate,
    dao.title,
    dao.content,
)