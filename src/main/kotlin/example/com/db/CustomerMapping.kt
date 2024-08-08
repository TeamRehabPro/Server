package example.com.db

import example.com.customerModel.Customer
import example.com.customerModel.Gender
import example.com.customerModel.Injury
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object CustomerTable : IntIdTable("customers") {
    val customerName = varchar("customerName", 50)
    val customerGender = varchar("customerGender", 50)
    val customerBirthDate = date("customerBirthDate")
    val customerPhoneNumber = varchar("customerPhoneNumber", 50)
    val injury = varchar("injury", 50)
}

class CustomerDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CustomerDAO>(CustomerTable)

    var customerName by CustomerTable.customerName
    var customerGender by CustomerTable.customerGender
    var customerBirthDate by CustomerTable.customerBirthDate
    var customerPhoneNumber by CustomerTable.customerPhoneNumber
    var injury by CustomerTable.injury
}

suspend fun <T> customerSuspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun customerDaoToModel(dao: CustomerDAO) = Customer(
    dao.customerName,
    Gender.valueOf(dao.customerGender),
    dao.customerBirthDate,
    dao.customerPhoneNumber,
    Injury.valueOf(dao.injury),
)