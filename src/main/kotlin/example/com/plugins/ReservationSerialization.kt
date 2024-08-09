package example.com.plugins

import example.com.customerModel.Gender
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
            get("/id/{employeeId}") {
                val id = call.parameters["employeeId"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val reservations = repository.reservationById(id)

                    if (reservations.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(reservations)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
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