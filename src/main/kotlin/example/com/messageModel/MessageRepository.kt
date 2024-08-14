package example.com.messageModel


interface MessageRepository {
    suspend fun allMessages(): List<Message>
    suspend fun messagesBySender(sender: String): List<Message>
    suspend fun messagesByReceiver(receiver: String): List<Message>
    suspend fun addMessage(message: Message)
}