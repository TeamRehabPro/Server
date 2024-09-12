package example.com.db

import example.com.messageModel.Message
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object MessageTable : IntIdTable("message") {
    val sender = varchar("sender", 50)
    val receiver = varchar("receiver", 50)
    val title = varchar("title", 50)
    val content = varchar("content", 255)
    val creationDate = date("creation_date")
}

class MessageDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MessageDAO>(MessageTable)

    var sender by MessageTable.sender
    var receiver by MessageTable.receiver
    var title by MessageTable.title
    var content by MessageTable.content
    var creationDate by MessageTable.creationDate
}

suspend fun <T> messageSuspendTransaction(block: Transaction.() -> T): T =
    runSuspendTransaction(block)

fun messageDaoToModel(dao: MessageDAO) = Message(
    dao.sender,
    dao.receiver,
    dao.title,
    dao.content,
    dao.creationDate,
)