package com.example.shoppy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy.R
import com.example.shoppy.adapter.favoriteAdapter
import com.example.shoppy.model.ShoppingItem
import com.example.shoppy.repository.shoppingRepositoryImpl
import com.example.shoppy.respository.UserRepositoryImp
import com.example.shoppy.viewmodel.ShoppingItemViewModel
import com.example.week3.viewmodel.UserViewModel

class favoriteFragment : Fragment() {

    private lateinit var shoppingItemViewModel: ShoppingItemViewModel
    private lateinit var favoriteItemsAdapter: favoriteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var userViewModel: UserViewModel
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        // Initialize RecyclerView and ViewModel
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the UserViewModel and ShoppingItemViewModel
        userViewModel = UserViewModel(UserRepositoryImp())

        // Get the current user ID
        val currentUser = userViewModel.getCurrentUser()
        userId = currentUser?.uid ?: ""

        // Initialize ShoppingItemViewModel
        shoppingItemViewModel = ShoppingItemViewModel(shoppingRepositoryImpl())

        // Initialize the FavoriteItemsAdapter
        favoriteItemsAdapter = favoriteAdapter(emptyList(), object : favoriteAdapter.OnItemClickListener {
            override fun onFavoriteToggle(item: ShoppingItem, isFavorite: Boolean) {
                // Toggle the favorite status in the ViewModel
            }
        })

        // Set the adapter for RecyclerView
        recyclerView.adapter = favoriteItemsAdapter

        // Observe favorite items from the ViewModel
        shoppingItemViewModel.getFavoriteItems(userId)

        shoppingItemViewModel.favoriteItems.observe(viewLifecycleOwner, Observer { favoriteItems ->
            if (favoriteItems != null && favoriteItems.isNotEmpty()) {
                // Update the adapter with the favorite items
                favoriteItemsAdapter.updateItems(favoriteItems)
            } else {
                // Handle the case where no favorite items are found
                Toast.makeText(context, "No favorite items found", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }
}
