package example.com.reservationModel

interface ReservationRepository {
    suspend fun addReservation(reservation: Reservation)
}