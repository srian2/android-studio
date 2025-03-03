package com.example.shoppy.repository

import android.util.Log
import com.example.shoppy.model.ShoppingItem
import com.google.firebase.database.*

class shoppingRepositoryImpl : shoppingRepository {

    private val database = FirebaseDatabase.getInstance()
    private val shoppingRef = database.reference.child("shopping_items")

    override fun addItem(userId: String, item: ShoppingItem, callback: (Boolean, String) -> Unit) {
        if (userId.isEmpty()) {
            callback(false, "User ID is missing")
            return
        }

        // Generate a unique itemId for this shopping item
        val itemId = shoppingRef.child(userId).push().key ?: return callback(false, "Failed to generate ID")
        item.id = itemId  // Assign the generated key to the item

        // Add the userId to the item data (this will be stored in Firebase)
        item.userId = userId

        if (item.favorite == null) {
            item.favorite = false
        }
        // Now push the item data under the generated itemId
        shoppingRef.child(userId).child(itemId).setValue(item)
            .addOnSuccessListener {
                callback(true, "Item added successfully")
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to add item")
            }
    }


    override fun getItems(userId: String, callback: (List<ShoppingItem>?, Boolean, String) -> Unit) {
        shoppingRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList = mutableListOf<ShoppingItem>()
                for (child in snapshot.children) {
                    val item = child.getValue(ShoppingItem::class.java)
                    item?.let { itemList.add(it) }
                    Log.d("edd","tems from repo:$item")
                }
                callback(itemList, true, "Items fetched successfully")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }


    override fun updateItem(userId: String, itemId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        shoppingRef.child(userId).child(itemId).updateChildren(data)
            .addOnSuccessListener { callback(true, "Item updated successfully") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to update item") }
    }

    override fun deleteItem(userId: String, itemId: String, callback: (Boolean, String) -> Unit) {
        shoppingRef.child(userId).child(itemId).removeValue()
            .addOnSuccessListener { callback(true, "Item deleted successfully") }
            .addOnFailureListener { e -> callback(false, e.message ?: "Failed to delete item") }
    }
    override fun toggleFavorite(userId: String, itemId: String, isFavorite: Boolean, callback: (Boolean, String) -> Unit) {
        // Prepare the update data as a map
        val updates = mapOf("favorite" to isFavorite)

        Log.d("dfd", "value from repo:$isFavorite")

        shoppingRef.child(userId).child(itemId).updateChildren(updates)
            .addOnSuccessListener {
                callback(true, "Favorite status updated")
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to update favorite status")
            }
    }



    override fun getFavoriteItems(userId: String, callback: (List<ShoppingItem>?, Boolean, String) -> Unit) {
        shoppingRef.child(userId).orderByChild("favorite").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favoritesList = mutableListOf<ShoppingItem>()
                    for (child in snapshot.children) {
                        val item = child.getValue(ShoppingItem::class.java)
                        item?.let { favoritesList.add(it) }
                    }
                    callback(favoritesList, true, "Favorite items fetched successfully")
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null, false, error.message)
                }
            })
    }


}
