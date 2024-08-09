package example.com.plugins

import example.com.employeeModel.Employee
import example.com.employeeModel.EmployeeRepository
import example.com.employeeModel.Position
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.employeeConfigureSerialization(repository: EmployeeRepository) {
    routing {
        route("/employees") {
            get {
                val employees = repository.allEmployees()
                call.respond(employees)
            }
            get("/name/{employeeName}") {
                val name = call.parameters["employeeName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val employee = repository.employeeByName(name)
                if (employee == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(employee)
            }
            get("/position/{position}") {
                val positionAsText = call.parameters["position"]
                if (positionAsText == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val position = Position.valueOf(positionAsText)
                    val employees = repository.employeesByPosition(position)

                    if (employees.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(employees)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post {
                try {
                    val employee = call.receive<Employee>()
                    repository.addEmployee(employee)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete("/{employeeName}") {
                val name = call.parameters["employeeName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (repository.removeEmployee(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
