package example.com.plugins

import example.com.profileModel.Profile
import example.com.profileModel.ProfileRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.profileConfigureSerialization(repository: ProfileRepository) {
    routing {
        route("/profile") {
            get {
                val profiles = repository.allProfiles()
                call.respond(profiles)
            }
            get("/id/{employeeId}") {
                val employeeId = call.parameters["employeeId"]
                if (employeeId == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val profile = repository.profileById(employeeId)
                if (profile == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(profile)
            }

            put("/id/{employeeId}") {
                // 경로에서 employeeId 추출
                val employeeId = call.parameters["employeeId"] ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    "Missing or invalid employeeId"
                )

                // 요청 본문에서 Profile 데이터를 받아옴
                val profileRequest = call.receive<Profile>()

                // Profile 객체를 생성하여 editProfile 함수 호출
                val profile = Profile(
                    employeeId = employeeId,
                    employeeName = profileRequest.employeeName,
                    dateOfBirth = profileRequest.dateOfBirth,
                    address = profileRequest.address,
                    position = profileRequest.position // 필요한 경우 position도 설정
                )

                // Profile 수정
                try {
                    repository.editProfile(profile)
                    call.respond(HttpStatusCode.OK, "Profile updated successfully")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Failed to update profile: ${e.message}")
                }
            }

        }
    }
}