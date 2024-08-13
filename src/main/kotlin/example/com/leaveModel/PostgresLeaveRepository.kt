package example.com.leaveModel

import example.com.db.LeaveDAO
import example.com.db.LeaveTable
import example.com.db.leaveDaoToModel
import example.com.db.leaveSuspendTransaction
import java.time.LocalDate


class PostgresLeaveRepository : LeaveRepository {
    override suspend fun allLeaves(): List<Leave> = leaveSuspendTransaction {
        LeaveDAO.all().map(::leaveDaoToModel)
    }

    override suspend fun leavesByDate(date: LocalDate): List<Leave> = leaveSuspendTransaction {
        LeaveDAO
            .find { (LeaveTable.date eq date) }
            .map(::leaveDaoToModel)
    }

    override suspend fun addLeaves(leave: Leave): Unit = leaveSuspendTransaction {
        LeaveDAO.new {
            employeeId = leave.employeeId
            date = leave.date
            leaveType = leave.leaveType.toString()
            comment = leave.comment
        }
    }
}