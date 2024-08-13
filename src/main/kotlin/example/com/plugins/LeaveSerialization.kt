package example.com.plugins

import example.com.leaveModel.Leave
import example.com.leaveModel.LeaveRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun Application.leaveConfigureSerialization(repository: LeaveRepository) {
    routing {
        route("/leave") {
            get {
                val leaves = repository.allLeaves()
                call.respond(leaves)
            }
            get("/date/{date}") {
                val date = call.parameters["date"]
                if (date == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val parsedDate = LocalDate.parse(date)

                    val leaves = repository.leavesByDate(parsedDate)
                    if (leaves.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound, "No reservations found for the given id and date")
                        return@get
                    }
                    call.respond(leaves)
                } catch (ex: DateTimeParseException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid date format")
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
            post {
                try {
                    val leave = call.receive<Leave>()
                    repository.addLeaves(leave)
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