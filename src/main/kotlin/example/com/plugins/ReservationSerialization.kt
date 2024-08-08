package example.com.plugins

import example.com.employeeModel.Employee
import example.com.reservationModel.Reservation
import example.com.reservationModel.ReservationRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.reservationConfigureSerialization(repository: ReservationRepository) {
    routing {
        route("/reservations") {
            get {
                val reservations = repository.allReservations()
                call.respond(reservations)
            }
            post {
                try {
                    val reservation = call.receive<Reservation>()
                    repository.addReservation(reservation)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}