package example.com.customerModel

import example.com.db.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

enum class Injury {
    Upper, Lower, Spine
}

enum class Gender {
    Male, Female
}

@Serializable
data class Customer(
    val customerName: String,
    val customerGender: Gender,
    @Serializable(with = LocalDateSerializer::class) val customerBirthDate: LocalDate,
    val customerPhoneNumber: String,
    val injury: Injury
)