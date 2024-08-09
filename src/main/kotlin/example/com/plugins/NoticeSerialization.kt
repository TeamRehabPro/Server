package example.com.plugins

import example.com.noticeModel.Notice
import example.com.noticeModel.NoticeRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.noticeConfigureSerialization(repository: NoticeRepository) {
    routing {
        route("/notice") {
            get {
                val notice = repository.allNotice()
                call.respond(notice)
            }
            post {
                try {
                    val notice = call.receive<Notice>()
                    repository.addNotice(notice)
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