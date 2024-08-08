package example.com.employeeModel

interface EmployeeRepository {
    suspend fun allEmployees(): List<Employee>
    suspend fun employeesByPosition(position: Position): List<Employee>
    suspend fun employeeByName(name: String): Employee?
    suspend fun addEmployee(employee: Employee)
    suspend fun removeEmployee(name: String): Boolean
}