package example.com.plugins

import example.com.reservationModel.Reservation
import example.com.reservationModel.ReservationRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate
import java.time.format.DateTimeParseException

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
                    val reservations = repository.reservationsById(id)

                    if (reservations.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(reservations)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            get("/id/{employeeId}/date/{reservationDate}") {
                val id = call.parameters["employeeId"]
                val date = call.parameters["reservationDate"]

                if (id == null || date == null) {
                    call.respond(HttpStatusCode.BadRequest, "Missing id or date")
                    return@get
                }

                try {
                    // 날짜를 파싱하여 LocalDate로 변환
                    val parsedDate = LocalDate.parse(date) // 예: "2024-08-09" 형식의 날짜

                    // employeeId와 reservationDate로 예약을 검색
                    val reservations = repository.reservationsByIdAndDate(id, parsedDate)

                    if (reservations.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound, "No reservations found for the given id and date")
                        return@get
                    }
                    call.respond(reservations)
                } catch (ex: DateTimeParseException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid date format")
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