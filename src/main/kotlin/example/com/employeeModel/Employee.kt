package example.com.employeeModel

import example.com.db.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

enum class Position {
    Manager, General,
}

@Serializable
data class Employee(
    val employeeId: String,
    val employeeName: String,
    val employeePassword: String,
    val position: Position,
    val employeePhoneNumber: String,
    @Serializable(with = LocalDateSerializer::class) val joiningDate: LocalDate,
)