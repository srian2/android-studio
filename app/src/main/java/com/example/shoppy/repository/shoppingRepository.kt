package com.example.shoppy.repository

import com.example.shoppy.model.ShoppingItem

interface shoppingRepository {
    fun addItem(userId: String, item: ShoppingItem, callback: (Boolean, String) -> Unit)
    fun getItems(userId: String, callback: (List<ShoppingItem>?, Boolean, String) -> Unit)
    fun updateItem(
        userId: String,
        itemId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    )

    fun deleteItem(userId: String, itemId: String, callback: (Boolean, String) -> Unit)
    fun toggleFavorite(
        userId: String,
        itemId: String,
        isFavorite: Boolean,
        callback: (Boolean, String) -> Unit
    )


    fun getFavoriteItems(userId: String, callback: (List<ShoppingItem>?, Boolean, String) -> Unit)

}
