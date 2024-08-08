package example.com.employeeModel

import example.com.db.EmployeeDAO
import example.com.db.EmployeeTable
import example.com.db.employeeDaoToModel
import example.com.db.employeeSuspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresEmployeeRepository : EmployeeRepository {
    override suspend fun allEmployees(): List<Employee> = employeeSuspendTransaction {
        EmployeeDAO.all().map(::employeeDaoToModel)
    }

    override suspend fun employeesByPosition(position: Position): List<Employee> = employeeSuspendTransaction {
        EmployeeDAO
            .find { (EmployeeTable.position eq position.toString()) }
            .map(::employeeDaoToModel)
    }

    override suspend fun employeeByName(name: String): Employee? = employeeSuspendTransaction {
        EmployeeDAO
            .find { (EmployeeTable.employeeName eq name) }
            .limit(1)
            .map(::employeeDaoToModel)
            .firstOrNull()
    }

    override suspend fun addEmployee(employee: Employee): Unit = employeeSuspendTransaction {
        EmployeeDAO.new {
            employeeId = employee.employeeId
            employeeName = employee.employeeName
            employeePassword = employee.employeePassword
            position = employee.position.toString()
            employeePhoneNumber = employee.employeePhoneNumber
            joiningDate = employee.joiningDate
        }
    }

    override suspend fun removeEmployee(name: String): Boolean = employeeSuspendTransaction {
        val rowsDeleted = EmployeeTable.deleteWhere {
            EmployeeTable.employeeName eq name
        }
        rowsDeleted == 1
    }
}