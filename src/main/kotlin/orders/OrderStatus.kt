package org.orders

enum class OrderStatus(val displayName: String, val canTransitionTo: List<OrderStatus>) {
    PENDING("Ожидает обработки", listOf(CONFIRMED, CANCELLED)),
    CONFIRMED("Подтверждён", listOf(PROCESSING, CANCELLED)),
    PROCESSING("В обработке", listOf(SHIPPED, CANCELLED)),
    SHIPPED("Доставляется", listOf(DELIVERED, CANCELLED)),
    DELIVERED("Доставлен", listOf()),
    CANCELLED("Отменён", listOf());

    fun canTransitionTo(newStatus: OrderStatus): Boolean {
        return newStatus in canTransitionTo
    }
}

sealed class OrderOperationResult {
    data class Success(val message: String, val orderId: String) : OrderOperationResult()
    data class Error(val errorMessage: String, val reason: ErrorReason) : OrderOperationResult()

    enum class ErrorReason {
        INVALID_STATUS_TRANSITION,
        INSUFFICIENT_STOCK,
        USER_NOT_FOUND,
        PRODUCT_NOT_FOUND,
        INVALID_ORDER_DATA
    }
}