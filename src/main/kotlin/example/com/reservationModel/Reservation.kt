package example.com.reservationModel

import example.com.db.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

enum class Detail {
    Manual, Physical,
}

@Serializable
data class Reservation(
    val employeeId: String,
    val customerId: Int,
    @Serializable(with = LocalDateTimeSerializer::class) val reservationDate: LocalDateTime,
    val customerPhoneNumber: String,
    val detail: Detail,
    val comment: String
)