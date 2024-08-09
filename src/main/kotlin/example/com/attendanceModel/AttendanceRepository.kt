package example.com.attendanceModel

interface AttendanceRepository {
    suspend fun addAttendance(attendance: Attendance)
}