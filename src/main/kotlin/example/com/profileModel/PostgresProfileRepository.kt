package example.com.profileModel

import example.com.db.ProfileDAO
import example.com.db.ProfileTable
import example.com.db.profileDaoToModel
import example.com.db.profileSuspendTransaction

class PostgresProfileRepository : ProfileRepository {
    override suspend fun allProfiles(): List<Profile> = profileSuspendTransaction {
        ProfileDAO.all().map(::profileDaoToModel)
    }

    override suspend fun addProfile(profile: Profile): Unit = profileSuspendTransaction {
        ProfileDAO.new {
            employeeId = profile.employeeId
            position = profile.position.toString()
            employeeName = profile.employeeName
            dateOfBirth = profile.dateOfBirth
            address = profile.address
        }
    }

    override suspend fun profileById(employeeId: String): Profile? = profileSuspendTransaction {
        ProfileDAO
            .find { (ProfileTable.employeeId eq employeeId) }
            .limit(1)
            .map(::profileDaoToModel)
            .firstOrNull()
    }

    override suspend fun editProfile(profile: Profile): Unit = profileSuspendTransaction {
        // 기존 프로필을 찾기 위해 employeeId로 검색
        val profileToUpdate = ProfileDAO.find { ProfileTable.employeeId eq profile.employeeId }.firstOrNull()

        // 프로필이 존재할 경우 업데이트
        profileToUpdate?.apply {
            employeeName = profile.employeeName
            dateOfBirth = profile.dateOfBirth
            address = profile.address
        } ?: throw NoSuchElementException("Profile with employeeId ${profile.employeeId} not found.")
    }
}