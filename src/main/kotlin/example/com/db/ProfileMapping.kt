package example.com.db

import example.com.profileModel.Position
import example.com.profileModel.Profile
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object ProfileTable : IntIdTable("profile") {
    val employeeId = varchar("employee_id", 50)
    val position = varchar("position", 50)
    val employeeName = varchar("employee_name", 50)
    val dateOfBirth = date("date_of_birth")
    val address = varchar("address", 255)
}

class ProfileDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProfileDAO>(ProfileTable)

    var employeeId by ProfileTable.employeeId
    var position by ProfileTable.position
    var employeeName by ProfileTable.employeeName
    var dateOfBirth by ProfileTable.dateOfBirth
    var address by ProfileTable.address
}

suspend fun <T> profileSuspendTransaction(block: Transaction.() -> T): T =
    runSuspendTransaction(block)

fun profileDaoToModel(dao: ProfileDAO) = Profile(
    dao.employeeId,
    Position.valueOf(dao.position),
    dao.employeeName,
    dao.dateOfBirth,
    dao.address,
)