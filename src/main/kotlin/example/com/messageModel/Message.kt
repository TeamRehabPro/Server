package example.com.messageModel

import example.com.db.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Message(
    val sender: String,
    val receiver: String,
    val title: String,
    val content: String,
    @Serializable(with = LocalDateSerializer::class) val creationDate: LocalDate,
)