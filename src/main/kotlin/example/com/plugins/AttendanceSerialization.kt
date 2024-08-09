package example.com.plugins

import example.com.attendanceModel.Attendance
import example.com.attendanceModel.AttendanceRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.attendanceConfigureSerialization(repository: AttendanceRepository) {
    routing {
        route("/attendance") {
            get {
                val attendance = repository.allAttendance()
                call.respond(attendance)
            }
            post {
                try {
                    val attendance = call.receive<Attendance>()
                    repository.addAttendance(attendance)
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