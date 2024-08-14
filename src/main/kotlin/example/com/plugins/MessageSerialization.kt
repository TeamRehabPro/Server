package example.com.plugins

import example.com.messageModel.Message
import example.com.messageModel.MessageRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.messageConfigureSerialization(repository: MessageRepository) {
    routing {
        route("/messages") {
            get {
                val messages = repository.allMessages()
                call.respond(messages)
            }
            get("/sender/{sender}") {
                val sender = call.parameters["sender"]
                if (sender == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val message = repository.messagesBySender(sender)
                call.respond(message)
            }
            get("/receiver/{receiver}") {
                val receiver = call.parameters["receiver"]
                if (receiver == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val message = repository.messagesByReceiver(receiver)
                call.respond(message)
            }
            post {
                try {
                    val message = call.receive<Message>()
                    repository.addMessage(message)
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