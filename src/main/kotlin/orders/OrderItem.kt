package org.orders

import org.models.Product

data class OrderItem(
    val product: Product,
    val quantity: Int
) {
    init {
        require(quantity > 0) { "Количество должно быть положительным" }
    }

    val subtotal: Double
        get() = product.price * quantity

    override fun toString(): String {
        return "${product.name} x $quantity = ${"%.2f".format(subtotal)} руб."
    }
}