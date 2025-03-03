package com.example.shoppy.model

data class ShoppingItem(
    var id: String = "",
    var name: String = "",
    var quantity: Int = 1,
    var price: Long = 0,
    var userId: String = "",
    var favorite: Boolean? = false // Rename to match Firebase field
)
