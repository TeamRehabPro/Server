package example.com.plugins

import example.com.customerModel.Customer
import example.com.customerModel.CustomerRepository
import example.com.customerModel.Gender
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.customerConfigureSerialization(repository: CustomerRepository) {
    routing {
        route("/customers") {
            get {
                val customers = repository.allCustomers()
                call.respond(customers)
            }
            get("/byName/{customerName}") {
                val name = call.parameters["customerName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val customer = repository.customerByName(name)
                if (customer == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(customer)
            }
            get("/byGender/{customerGender}") {
                val genderAsText = call.parameters["customerGender"]
                if (genderAsText == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val gender = Gender.valueOf(genderAsText)
                    val customers = repository.customersByGender(gender)

                    if (customers.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(customers)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post {
                try {
                    val customer = call.receive<Customer>()
                    repository.addCustomer(customer)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete("/{customerName}") {
                val name = call.parameters["customerName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (repository.removeCustomer(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}