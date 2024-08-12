package example.com.noticeModel

import example.com.db.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Notice(
    val employeeId: String,
    @Serializable(with = LocalDateSerializer::class) val creationDate: LocalDate,
    val title: String,
    val content: String,
)