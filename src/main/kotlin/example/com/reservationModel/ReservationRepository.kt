package example.com.reservationModel


interface ReservationRepository {
    suspend fun allReservations(): List<Reservation>
    suspend fun addReservation(reservation: Reservation)
    suspend fun reservationsById(employeeId: String): List<Reservation>
}