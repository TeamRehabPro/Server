package example.com.messageModel

import example.com.db.MessageDAO
import example.com.db.MessageTable
import example.com.db.messageDaoToModel
import example.com.db.messageSuspendTransaction

class PostgresMessageRepository : MessageRepository {
    override suspend fun allMessages(): List<Message> = messageSuspendTransaction {
        MessageDAO.all().map(::messageDaoToModel)
    }

    override suspend fun addMessage(message: Message): Unit = messageSuspendTransaction {
        MessageDAO.new {
            sender = message.sender
            receiver = message.receiver
            title = message.title
            content = message.content
            creationDate = message.creationDate
        }
    }

    override suspend fun messagesBySender(sender: String): List<Message> = messageSuspendTransaction {
        MessageDAO
            .find { (MessageTable.sender eq sender) }
            .map(::messageDaoToModel)
    }

    override suspend fun messagesByReceiver(receiver: String): List<Message> = messageSuspendTransaction {
        MessageDAO
            .find { (MessageTable.receiver eq receiver) }
            .map(::messageDaoToModel)
    }
}