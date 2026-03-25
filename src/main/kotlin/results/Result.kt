package org.results

import org.orders.Order

sealed class Result<out T> {
    data class Success<T>(val data: T, val message: String = "Операция выполнена успешно") : Result<T>()
    data class Error(val message: String, val errorCode: Int) : Result<Nothing>()

    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error

    fun getOrNull(): T? = if (this is Success) data else null
}