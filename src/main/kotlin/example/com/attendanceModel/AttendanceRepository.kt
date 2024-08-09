package example.com.attendanceModel

interface AttendanceRepository {
    suspend fun allAttendance(): List<Attendance>
    suspend fun addAttendance(attendance: Attendance)
}