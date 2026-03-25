package org.users

class Admin(
    id: String,
    email: String,
    password: String,
    name: String,
    phone: String,
    val accessLevel: AdminAccessLevel
) : User(id, email, password, name, phone) {

    override fun getRole(): String = "Администратор"

    fun manageUsers() {
        println("$name управляет пользователями системы")
    }

    fun viewSystemStats() {
        println("Статистика системы доступна")
    }

    override fun toString(): String {
        return super.toString() + "\nУровень доступа: ${accessLevel.displayName}"
    }
}

enum class AdminAccessLevel(val displayName: String) {
    SUPER_ADMIN("Супер-администратор"),
    MODERATOR("Модератор"),
    SUPPORT("Поддержка")
}