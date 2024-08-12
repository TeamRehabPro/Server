package example.com.profileModel

import example.com.db.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

enum class Position {
    Manager, General
}

@Serializable
data class Profile(
    val employeeId: String,
    val position: Position,
    var employeeName: String,
    @Serializable(with = LocalDateSerializer::class) var dateOfBirth: LocalDate,
    var address: String,
)