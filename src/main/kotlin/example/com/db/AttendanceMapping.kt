package example.com.db

import example.com.attendanceModel.Attendance
import example.com.attendanceModel.Status
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.time
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object AttendanceTable : IntIdTable("attendance") {
    val employeeId = varchar("employeeId", 50)
    val date = date("date")
    val checkInTime = time("checkInTime")
    val checkOutTime = time("checkOutTime")
    val status = varchar("status", 20)
}

class AttendanceDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AttendanceDao>(AttendanceTable)

    var employeeId by AttendanceTable.employeeId
    var date by AttendanceTable.date
    var checkInTime by AttendanceTable.checkInTime
    var checkOutTime by AttendanceTable.checkOutTime
    var status by AttendanceTable.status
}

suspend fun <T> attendanceSuspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun attendanceDaoToModel(dao: AttendanceDao) = Attendance(
    dao.employeeId,
    dao.date,
    dao.checkInTime,
    dao.checkOutTime,
    Status.valueOf(dao.status),
)