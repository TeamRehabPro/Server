package example.com.customerModel


interface CustomerRepository {
    suspend fun allCustomers(): List<Customer>
    suspend fun customersByGender(gender: Gender): List<Customer>
    suspend fun customerByName(name: String): Customer?
    suspend fun addCustomer(customer: Customer)
    suspend fun removeCustomer(name: String): Boolean
}