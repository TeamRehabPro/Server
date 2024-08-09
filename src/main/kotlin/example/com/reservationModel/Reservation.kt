package example.com.reservationModel

import example.com.db.LocalDateSerializer
import example.com.db.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

enum class Detail {
    Manual, Physical,
}

@Serializable
data class Reservation(
    val employeeId: String,
    val customerId: Int,
    val customerPhoneNumber: String,
    val detail: Detail,
    val comment: String,
    @Serializable(with = LocalDateSerializer::class) val reservationDate: LocalDate,
    @Serializable(with = LocalTimeSerializer::class) val reservationTime: LocalTime
)