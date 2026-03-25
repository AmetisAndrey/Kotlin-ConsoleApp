package org.models

data class Address(
    val street: String,
    val house: String,
    val apartment: String? = null,
    val city: String,
    val postalCode: String
) {
    override fun toString(): String {
        return "$city, ул. $street, д. $house${apartment?.let { ", кв. $it" } ?: ""}, $postalCode"
    }
}