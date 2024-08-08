package example.com.attendanceModel

import example.com.db.LocalDateSerializer
import example.com.db.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

enum class Status {
    Present, Late, Absent
}

@Serializable
data class Attendance(
    val employeeId: String,
    @Serializable(with = LocalDateSerializer::class) val date: LocalDate,
    @Serializable(with = LocalTimeSerializer::class) val checkInTime: LocalTime,
    @Serializable(with = LocalTimeSerializer::class) val checkOutTime: LocalTime,
    val status: Status,
)