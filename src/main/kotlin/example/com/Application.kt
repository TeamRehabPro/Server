package example.com

import example.com.attendanceModel.PostgresAttendanceRepository
import example.com.customerModel.PostgresCustomerRepository
import example.com.employeeModel.PostgresEmployeeRepository
import example.com.noticeModel.PostgresNoticeRepository
import example.com.plugins.*
import example.com.reservationModel.PostgresReservationRepository
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging)
    val employeeRepository = PostgresEmployeeRepository()
    val customerRepository = PostgresCustomerRepository()
    val reservationRepository = PostgresReservationRepository()
    val attendanceRepository = PostgresAttendanceRepository()
    val noticeRepository = PostgresNoticeRepository()
    employeeConfigureSerialization(employeeRepository)
    customerConfigureSerialization(customerRepository)
    reservationConfigureSerialization(reservationRepository)
    attendanceConfigureSerialization(attendanceRepository)
    noticeConfigureSerialization(noticeRepository)
    configureDatabases()
    configureRouting()
}
