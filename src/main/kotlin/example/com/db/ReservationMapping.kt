package example.com.db


import example.com.reservationModel.Detail
import example.com.reservationModel.Reservation
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.time
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object ReservationTable : IntIdTable("reservations") {
    val employeeId = varchar("employee_id", 50)
    val customerId = integer("customer_id")
    val customerPhoneNumber = varchar("customer_phone_number", 50)
    val detail = varchar("detail", 50)
    val comment = varchar("comment", 50)
    val reservationDate = date("reservation_date")
    val reservationTime = time("reservation_time")
    val customerName = varchar("customer_name",50)
}

class ReservationDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ReservationDAO>(ReservationTable)

    var employeeId by ReservationTable.employeeId
    var customerId by ReservationTable.customerId
    var customerPhoneNumber by ReservationTable.customerPhoneNumber
    var detail by ReservationTable.detail
    var comment by ReservationTable.comment
    var reservationDate by ReservationTable.reservationDate
    var reservationTime by ReservationTable.reservationTime
    var customerName by ReservationTable.customerName
}

suspend fun <T> reservationSuspendTransaction(block: Transaction.() -> T): T =
    runSuspendTransaction(block)


fun reservationDaoToModel(dao: ReservationDAO) = Reservation(
    dao.employeeId,
    dao.customerId,
    dao.customerPhoneNumber,
    Detail.valueOf(dao.detail),
    dao.comment,
    dao.reservationDate,
    dao.reservationTime,
    dao.customerName
)