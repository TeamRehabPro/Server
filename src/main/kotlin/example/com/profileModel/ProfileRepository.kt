package example.com.profileModel

interface ProfileRepository {
    suspend fun allProfiles(): List<Profile>
    suspend fun addProfile(profile: Profile)
    suspend fun profileById(employeeId: String): Profile?
    suspend fun editProfile(profile: Profile)
}