package example.com.leaveModel

import example.com.db.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

enum class LeaveType {
    Vacation, Seminar, BusinessTrip
}

@Serializable
data class Leave(
    val employeeId: String,
    @Serializable(with = LocalDateSerializer::class) val date: LocalDate,
    val leaveType: LeaveType,
    val comment: String,
)