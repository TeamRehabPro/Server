package example.com.reservationModel

import java.time.LocalDate


interface ReservationRepository {
    suspend fun allReservations(): List<Reservation>
    suspend fun addReservation(reservation: Reservation)
    suspend fun reservationsById(employeeId: String): List<Reservation>
    suspend fun reservationsByIdAndDate(employeeId: String, reservationDate: LocalDate): List<Reservation>
}