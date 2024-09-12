package example.com.db

import example.com.leaveModel.Leave
import example.com.leaveModel.LeaveType
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object LeaveTable : IntIdTable("leave") {
    val employeeId = varchar("employee_id", 50)
    val date = date("date")
    val leaveType = varchar("leave_type", 50)
    val comment = varchar("comment", 255)
}

class LeaveDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LeaveDAO>(LeaveTable)

    var employeeId by LeaveTable.employeeId
    var date by LeaveTable.date
    var leaveType by LeaveTable.leaveType
    var comment by LeaveTable.comment
}

suspend fun <T> leaveSuspendTransaction(block: Transaction.() -> T): T =
    runSuspendTransaction(block)

fun leaveDaoToModel(dao: LeaveDAO) = Leave(
    dao.employeeId,
    dao.date,
    LeaveType.valueOf(dao.leaveType),
    dao.comment,
)