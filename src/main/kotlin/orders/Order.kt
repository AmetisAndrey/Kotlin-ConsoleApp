package org.orders

import org.models.Address
import org.users.Customer
import java.util.UUID

class Order(
    val id: String = UUID.randomUUID().toString().substring(0, 8),
    val customer: Customer,
    val items: MutableList<OrderItem>,
    val deliveryAddress: Address,
    private var _status: OrderStatus = OrderStatus.PENDING
) {
    val status: OrderStatus
        get() = _status

    val totalAmount: Double
        get() = items.sumOf { it.subtotal }

    private val statusHistory = mutableListOf(_status to getCurrentTimestamp())

    init {
        items.forEach { item ->
            if (!item.product.isAvailable(item.quantity)) {
                throw IllegalArgumentException("Товар ${item.product.name} недоступен в указанном количестве")
            }
        }
    }

    fun changeStatus(newStatus: OrderStatus): OrderOperationResult {
        return if (_status.canTransitionTo(newStatus)) {
            val oldStatus = _status
            _status = newStatus
            statusHistory.add(newStatus to getCurrentTimestamp())

            if (newStatus == OrderStatus.CONFIRMED) {
                items.forEach { it.product.reduceStock(it.quantity) }
                customer.addLoyaltyPoints((totalAmount / 100).toInt())
            }

            OrderOperationResult.Success(
                "Статус заказа изменён с ${oldStatus.displayName} на ${newStatus.displayName}",
                id
            )
        } else {
            OrderOperationResult.Error(
                "Невозможно перевести заказ из статуса ${_status.displayName} в ${newStatus.displayName}",
                OrderOperationResult.ErrorReason.INVALID_STATUS_TRANSITION
            )
        }
    }

    fun cancel(): OrderOperationResult {
        return if (_status.canTransitionTo(OrderStatus.CANCELLED)) {
            changeStatus(OrderStatus.CANCELLED)
        } else {
            OrderOperationResult.Error(
                "Невозможно отменить заказ в статусе ${_status.displayName}",
                OrderOperationResult.ErrorReason.INVALID_STATUS_TRANSITION
            )
        }
    }

    fun getStatusHistory(): List<Pair<OrderStatus, String>> = statusHistory.toList()

    private fun getCurrentTimestamp(): String {
        return java.time.LocalDateTime.now().toString()
    }

    override fun toString(): String {
        return """
            |Заказ #$id
            |Клиент: ${customer.name}
            |Статус: ${_status.displayName}
            |Товары:
            |${items.joinToString("\n") { "  - $it" }}
            |Итого: ${"%.2f".format(totalAmount)} руб.
            |Адрес доставки: $deliveryAddress
        """.trimMargin()
    }
}