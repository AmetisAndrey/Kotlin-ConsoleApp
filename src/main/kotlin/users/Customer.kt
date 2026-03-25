package org.users

import org.models.Address

class Customer(
    id: String,
    email: String,
    password: String,
    name: String,
    phone: String,
    var address: Address? = null,
    val registrationDate: String = java.time.LocalDate.now().toString()
) : User(id, email, password, name, phone) {

    private val orderHistory = mutableListOf<String>()
    var loyaltyPoints: Int = 0
        private set

    override fun getRole(): String = "Клиент"

    fun addToOrderHistory(orderId: String) {
        orderHistory.add(orderId)
    }

    fun getOrderHistory(): List<String> = orderHistory.toList()

    fun addLoyaltyPoints(points: Int) {
        loyaltyPoints += points
    }

    fun redeemLoyaltyPoints(points: Int): Boolean {
        return if (loyaltyPoints >= points) {
            loyaltyPoints -= points
            true
        } else {
            false
        }
    }

    fun updateAddress(newAddress: Address) {
        address = newAddress
        println("Адрес успешно обновлён")
    }

    override fun toString(): String {
        return super.toString() + "\nАдрес: ${address ?: "не указан"}\nБонусные баллы: $loyaltyPoints"
    }
}