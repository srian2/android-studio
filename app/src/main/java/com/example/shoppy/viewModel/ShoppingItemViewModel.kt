package com.example.shoppy.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppy.model.ShoppingItem
import com.example.shoppy.repository.shoppingRepositoryImpl
import com.google.firebase.auth.FirebaseAuth

class ShoppingItemViewModel(private val repo: shoppingRepositoryImpl) : ViewModel() {

    private val _itemsList = MutableLiveData<List<ShoppingItem>>()
    val itemsList: LiveData<List<ShoppingItem>> = _itemsList

    private val _operationStatus = MutableLiveData<String>()
    val operationStatus: LiveData<String> = _operationStatus

    // Add Item to Firebase
    fun addItem(userId: String, item: ShoppingItem) {
        repo.addItem(userId, item) { success, message ->
            if (success) {
                _operationStatus.value = "Item added successfully"
            } else {
                _operationStatus.value = "Error: $message"
            }
        }
    }

    // Fetch Items from Firebase
    fun getItems(userId: String) {
        repo.getItems(userId) { items, success, message ->
            if (success) {
                _itemsList.value = items
            } else {
                _operationStatus.value = "Error: $message"
            }
        }
    }

    // Update Item in Firebase
    fun updateItem(userId: String, itemId: String, data: MutableMap<String, Any>) {
        repo.updateItem(userId, itemId, data) { success, message ->
            if (success) {
                _operationStatus.value = "Item updated successfully"
            } else {
                _operationStatus.value = "Error: $message"
            }
        }
    }

    // Delete Item from Firebase
    fun deleteItem(userId: String, itemId: String) {
        repo.deleteItem(userId, itemId) { success, message ->
            if (success) {
                _operationStatus.value = "Item deleted successfully"
                getItems(userId)
            } else {
                _operationStatus.value = "Error: $message"
            }
        }
    }


    fun toggleFavorite(userId: String, itemId: String, isFavorite: Boolean) {
        repo.toggleFavorite(userId, itemId, isFavorite) { success, message ->
            if (success) {
                _operationStatus.value = "Favorite status updated"
                getItems(userId) // Refresh items after update to reflect the change
            } else {
                _operationStatus.value = "Error: $message"
            }
        }
    }


    private val _favoriteItems = MutableLiveData<List<ShoppingItem>>()
    val favoriteItems: LiveData<List<ShoppingItem>> get() = _favoriteItems

    // Function to get favorite items from the repository
    fun getFavoriteItems(userId: String) {
        // Show loading status or any pre-fetching state
        repo.getFavoriteItems(userId) { items, success, message ->
            if (success) {
                // Update LiveData with the list of favorite items
                _favoriteItems.value = items
            }
            // Notify operation status (success or failure) with message
            _operationStatus.value = "Favorite status updated"
        }
    }



}
