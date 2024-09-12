package example.com.db

import example.com.employeeModel.Employee
import example.com.employeeModel.Position
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.javatime.date

object EmployeeTable : IntIdTable("employees") {
    val employeeId = varchar("employee_id", 50).uniqueIndex()
    val employeeName = varchar("employee_name", 50)
    val employeePassword = varchar("employee_password", 50)
    val position = varchar("position", 50)
    val employeePhoneNumber = varchar("employee_phone_number", 50)
    val joiningDate = date("joining_date")
}

class EmployeeDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EmployeeDAO>(EmployeeTable)

    var employeeId by EmployeeTable.employeeId
    var employeeName by EmployeeTable.employeeName
    var employeePassword by EmployeeTable.employeePassword
    var position by EmployeeTable.position
    var employeePhoneNumber by EmployeeTable.employeePhoneNumber
    var joiningDate by EmployeeTable.joiningDate
}

suspend fun <T> employeeSuspendTransaction(block: Transaction.() -> T): T =
    runSuspendTransaction(block)


fun employeeDaoToModel(dao: EmployeeDAO) = Employee(
    dao.employeeId,
    dao.employeeName,
    dao.employeePassword,
    Position.valueOf(dao.position),
    dao.employeePhoneNumber,
    dao.joiningDate,
)

