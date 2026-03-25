package org.users

import org.models.Address

abstract class User(
    val id: String,
    val email: String,
    protected var password: String,
    val name: String,
    val phone: String
) {
    private var isActive = true

    open fun authenticate(inputPassword: String): Boolean {
        return isActive && password == inputPassword
    }

    fun changePassword(oldPassword: String, newPassword: String): Boolean {
        return if (authenticate(oldPassword)) {
            password = newPassword
            true
        } else {
            false
        }
    }

    fun deactivate() {
        isActive = false
    }

    fun activate() {
        isActive = true
    }

    fun isAccountActive(): Boolean = isActive

    abstract fun getRole(): String

    override fun toString(): String {
        return "$name ($email) - $getRole"
    }
}