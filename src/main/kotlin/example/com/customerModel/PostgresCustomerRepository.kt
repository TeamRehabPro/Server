package example.com.customerModel

import example.com.db.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import example.com.db.customerSuspendTransaction

class PostgresCustomerRepository : CustomerRepository {
    override suspend fun allCustomers(): List<Customer> = customerSuspendTransaction {
        CustomerDAO.all().map(::customerDaoToModel)
    }

    override suspend fun customersByGender(gender: Gender): List<Customer> = customerSuspendTransaction {
        CustomerDAO
            .find { (CustomerTable.customerGender eq gender.toString()) }
            .map(::customerDaoToModel)
    }

    override suspend fun customerByName(name: String): Customer? = customerSuspendTransaction {
        CustomerDAO
            .find { (CustomerTable.customerName eq name) }
            .limit(1)
            .map(::customerDaoToModel)
            .firstOrNull()
    }

    override suspend fun addCustomer(customer: Customer): Unit = customerSuspendTransaction {
        CustomerDAO.new {
            customerName = customer.customerName
            customerGender = customer.customerGender.toString()
            customerBirthDate = customer.customerBirthDate
            customerPhoneNumber = customer.customerPhoneNumber
            injury = customer.injury.toString()
        }
    }

    override suspend fun removeCustomer(name: String): Boolean = customerSuspendTransaction {
        val rowsDeleted = CustomerTable.deleteWhere {
            CustomerTable.customerName eq name
        }
        rowsDeleted == 1
    }
}