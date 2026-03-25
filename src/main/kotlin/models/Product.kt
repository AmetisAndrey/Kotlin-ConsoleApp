package org.models

data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val category: ProductCategory,
    var stockQuantity: Int
) {
    init {
        require(price > 0) { "Цена должна быть положительной" }
        require(stockQuantity >= 0) { "Количество на складе не может быть отрицательным" }
    }

    fun isAvailable(quantity: Int): Boolean = stockQuantity >= quantity

    fun reduceStock(quantity: Int) {
        if (isAvailable(quantity)) {
            stockQuantity -= quantity
        } else {
            throw IllegalArgumentException("Недостаточно товара на складе")
        }
    }

    override fun toString(): String {
        return "$name (${category.displayName}) - ${"%.2f".format(price)} руб."
    }
}

enum class ProductCategory(val displayName: String) {
    ELECTRONICS("Электроника"),
    CLOTHING("Одежда"),
    BOOKS("Книги"),
    FOOD("Продукты питания"),
    SPORTS("Спорт и отдых"),
    OTHER("Другое")
}