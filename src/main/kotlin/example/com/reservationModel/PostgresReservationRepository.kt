package example.com.reservationModel

import example.com.db.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import java.time.LocalDate

class PostgresReservationRepository : ReservationRepository {
    override suspend fun allReservations(): List<Reservation> = reservationSuspendTransaction {
        ReservationDAO.all().map(::reservationDaoToModel)
    }

    override suspend fun addReservation(reservation: Reservation): Unit {
        reservationSuspendTransaction {
            // Check if the employee exists
            val employeeExists = EmployeeTable.select { EmployeeTable.employeeId eq reservation.employeeId }.count() > 0
            if (!employeeExists) throw IllegalArgumentException("Employee with id ${reservation.employeeId} does not exist")

            // Check if the customer exists
            val customer = CustomerTable.select { CustomerTable.id eq reservation.customerId }.singleOrNull()
            if (customer == null) throw IllegalArgumentException("Customer with id ${reservation.customerId} does not exist")

            // Create new reservation
            ReservationDAO.new {
                employeeId = reservation.employeeId
                customerId = reservation.customerId
                customerPhoneNumber =
                    customer[CustomerTable.customerPhoneNumber]  // Get phone number from customer table
                detail = reservation.detail.toString()
                comment = reservation.comment
                reservationDate = reservation.reservationDate
                reservationTime = reservation.reservationTime
            }
        }
    }

    override suspend fun reservationsById(employeeId: String): List<Reservation> = reservationSuspendTransaction {
        ReservationDAO
            .find { (ReservationTable.employeeId eq employeeId) }
            .map(::reservationDaoToModel)
    }

    override suspend fun reservationsByIdAndDate(employeeId: String, reservationDate: LocalDate): List<Reservation> =
        reservationSuspendTransaction {
            ReservationDAO
                .find { (ReservationTable.employeeId eq employeeId) and (ReservationTable.reservationDate eq reservationDate) }
                .map(::reservationDaoToModel)
        }
}