package example.com.leaveModel

import java.time.LocalDate

interface LeaveRepository {
    suspend fun allLeaves(): List<Leave>
    suspend fun leavesByDate(date: LocalDate): List<Leave>
    suspend fun addLeaves(leave: Leave)
}