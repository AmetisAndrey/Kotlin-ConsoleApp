import org.models.Address
import org.models.Product
import org.models.ProductCategory
import org.orders.Order
import org.orders.OrderItem
import org.orders.OrderStatus
import org.results.Result
import org.users.Admin
import org.users.AdminAccessLevel
import org.users.Customer

fun main() {
    println("╔══════════════════════════════════════════════════════════════╗")
    println("║       УПРАВЛЕНИЕ ЗАКАЗАМИ В ОНЛАЙН-МАГАЗИНЕ               ║")
    println("╚══════════════════════════════════════════════════════════════╝")

    println("\n--- СОЗДАНИЕ ТОВАРОВ ---")
    val laptop = Product(
        id = "P001",
        name = "Ноутбук Lenovo",
        price = 45000.0,
        category = ProductCategory.ELECTRONICS,
        stockQuantity = 10
    )

    val phone = Product(
        id = "P002",
        name = "Смартфон Samsung",
        price = 35000.0,
        category = ProductCategory.ELECTRONICS,
        stockQuantity = 5
    )

    val book = Product(
        id = "P003",
        name = "Kotlin в действии",
        price = 1500.0,
        category = ProductCategory.BOOKS,
        stockQuantity = 20
    )

    val tshirt = Product(
        id = "P004",
        name = "Футболка хлопковая",
        price = 800.0,
        category = ProductCategory.CLOTHING,
        stockQuantity = 50
    )

    println(laptop)
    println(phone)
    println(book)
    println(tshirt)

    println("\n--- СОЗДАНИЕ ПОЛЬЗОВАТЕЛЕЙ ---")

    val address = Address(
        street = "Ленина",
        house = "15",
        apartment = "42",
        city = "Москва",
        postalCode = "101000"
    )

    val customer = Customer(
        id = "C001",
        email = "ivan@example.com",
        password = "password123",
        name = "Иван Петров",
        phone = "+7-999-123-4567",
        address = address
    )

    val admin = Admin(
        id = "A001",
        email = "admin@shop.ru",
        password = "admin123",
        name = "Анна Смирнова",
        phone = "+7-999-888-7777",
        accessLevel = AdminAccessLevel.SUPER_ADMIN
    )

    println(customer)
    println("\n${admin}")

    println("\n--- ПРОВЕРКА АУТЕНТИФИКАЦИИ ---")
    println("Аутентификация клиента: ${customer.authenticate("password123")}")
    println("Аутентификация админа: ${admin.authenticate("admin123")}")

    println("\n--- СОЗДАНИЕ ЗАКАЗА ---")

    val orderItems = mutableListOf(
        OrderItem(laptop, 1),
        OrderItem(book, 2),
        OrderItem(tshirt, 3)
    )

    val order = Order(
        customer = customer,
        items = orderItems,
        deliveryAddress = address
    )

    println(order)

    println("\n--- ИЗМЕНЕНИЕ СТАТУСОВ ЗАКАЗА ---")

    val operations = listOf(
        { order.changeStatus(OrderStatus.CONFIRMED) },
        { order.changeStatus(OrderStatus.PROCESSING) },
        { order.changeStatus(OrderStatus.SHIPPED) },
        { order.changeStatus(OrderStatus.DELIVERED) }
    )

    operations.forEach { operation ->
        val result = operation()
        when (result) {
            is orders.OrderOperationResult.Success -> {
                println("✅ ${result.message}")
            }
            is orders.OrderOperationResult.Error -> {
                println("❌ ${result.errorMessage} (${result.reason})")
            }
        }
    }

    println("\n--- ПОПЫТКА НЕКОРРЕКТНОГО ПЕРЕХОДА ---")
    val invalidResult = order.changeStatus(OrderStatus.PENDING)
    when (invalidResult) {
        is orders.OrderOperationResult.Success -> println("✅ ${invalidResult.message}")
        is orders.OrderOperationResult.Error -> println("❌ ${invalidResult.errorMessage} (${invalidResult.reason})")
    }

     println("\n--- ИСПОЛЬЗОВАНИЕ SEALED CLASS RESULT ---")

    fun findProductById(productId: String): Result<Product> {
        val products = listOf(laptop, phone, book, tshirt)
        return products.find { it.id == productId }
            ?.let { Result.Success(it, "Товар найден") }
            ?: Result.Error("Товар с ID $productId не найден", 404)
    }

    val foundProduct = findProductById("P001")
    when (foundProduct) {
        is Result.Success -> {
            println("✅ ${foundProduct.message}: ${foundProduct.data}")
        }
        is Result.Error -> {
            println("❌ ${foundProduct.message} (Код: ${foundProduct.errorCode})")
        }
    }

    val notFoundProduct = findProductById("P999")
    when (notFoundProduct) {
        is Result.Success -> println("✅ ${notFoundProduct.message}")
        is Result.Error -> println("❌ ${notFoundProduct.message} (Код: ${notFoundProduct.errorCode})")
    }

    println("\n--- ПОЛИМОРФИЗМ: РАЗНЫЕ ТИПЫ ПОЛЬЗОВАТЕЛЕЙ ---")

    val users = listOf(customer, admin)

    users.forEach { user ->
        println("Пользователь: ${user.name}")
        println("  Роль: ${user.getRole()}")
        println("  Активен: ${user.isAccountActive()}")
        if (user is Admin) {
            println("  Уровень доступа: ${user.accessLevel.displayName}")
        }
        if (user is Customer) {
            println("  Бонусные баллы: ${user.loyaltyPoints}")
            println("  История заказов: ${user.getOrderHistory().size} заказов")
        }
        println()
    }

    println("\n--- ИСТОРИЯ СТАТУСОВ ЗАКАЗА ---")
    order.getStatusHistory().forEachIndexed { index, (status, timestamp) ->
        println("${index + 1}. ${status.displayName} - $timestamp")
    }

    println("\n--- ОСТАТКИ ТОВАРОВ ПОСЛЕ ПОДТВЕРЖДЕНИЯ ЗАКАЗА ---")
    println("Ноутбук Lenovo: ${laptop.stockQuantity} шт.")
    println("Kotlin в действии: ${book.stockQuantity} шт.")
    println("Футболка хлопковая: ${tshirt.stockQuantity} шт.")

    println("\n--- БОНУСНЫЕ БАЛЛЫ ---")
    println("Клиент ${customer.name} накопил ${customer.loyaltyPoints} бонусных баллов")

   println("\n--- ДЕМОНСТРАЦИЯ ОТМЕНЫ ЗАКАЗА ---")

    val orderToCancel = Order(
        customer = customer,
        items = mutableListOf(OrderItem(phone, 1)),
        deliveryAddress = address
    )

    println("Создан заказ #${orderToCancel.id} на сумму ${orderToCancel.totalAmount} руб.")
    println("Статус: ${orderToCancel.status.displayName}")

    val cancelResult = orderToCancel.cancel()
    when (cancelResult) {
        is orders.OrderOperationResult.Success -> {
            println("✅ ${cancelResult.message}")
            println("Новый статус: ${orderToCancel.status.displayName}")
        }
        is orders.OrderOperationResult.Error -> {
            println("❌ ${cancelResult.errorMessage}")
        }
    }

    println("\n╔══════════════════════════════════════════════════════════════╗")
    println("║                    ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА                    ║")
    println("╚══════════════════════════════════════════════════════════════╝")
}