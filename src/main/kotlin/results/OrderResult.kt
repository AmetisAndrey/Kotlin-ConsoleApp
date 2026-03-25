package org.results

import org.orders.Order
import org.orders.OrderOperationResult

class OrderResult private constructor(
    val order: Order?,
    val operationResult: OrderOperationResult,
    val timestamp: String = java.time.LocalDateTime.now().toString()
) {
    companion object {
        fun success(order: Order, message: String): OrderResult {
            return OrderResult(order, OrderOperationResult.Success(message, order.id))
        }

        fun error(order: Order?, reason: OrderOperationResult.ErrorReason, message: String): OrderResult {
            return OrderResult(order, OrderOperationResult.Error(message, reason))
        }
    }

    fun isSuccess(): Boolean = operationResult is OrderOperationResult.Success

    fun getMessage(): String = when (operationResult) {
        is OrderOperationResult.Success -> operationResult.message
        is OrderOperationResult.Error -> operationResult.errorMessage
    }
}