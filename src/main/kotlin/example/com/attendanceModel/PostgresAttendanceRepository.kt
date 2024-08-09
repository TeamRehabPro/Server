package example.com.attendanceModel

import example.com.db.*


class PostgresAttendanceRepository : AttendanceRepository {
    override suspend fun allAttendance(): List<Attendance> = attendanceSuspendTransaction {
        AttendanceDao.all().map(::attendanceDaoToModel)
    }

    override suspend fun addAttendance(attendance: Attendance): Unit = attendanceSuspendTransaction {
        AttendanceDao.new {
            employeeId = attendance.employeeId
            date = attendance.date
            checkInTime = attendance.checkInTime
            checkOutTime = attendance.checkOutTime
            status = attendance.status.toString()
        }
    }
}