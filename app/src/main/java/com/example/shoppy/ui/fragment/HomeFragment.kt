package com.example.shoppy.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy.R
import com.example.shoppy.adapter.ShoppingItemAdapter
import com.example.shoppy.model.ShoppingItem
import com.example.shoppy.repository.shoppingRepositoryImpl
import com.example.shoppy.respository.UserRepositoryImp
import com.example.shoppy.viewmodel.ShoppingItemViewModel
import com.example.week3.viewmodel.UserViewModel
class HomeFragment : Fragment() {

    private lateinit var shoppingItemViewModel: ShoppingItemViewModel
    private lateinit var shoppingItemAdapter: ShoppingItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var userViewModel: UserViewModel
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerView and ViewModel
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        userViewModel = UserViewModel(UserRepositoryImp())

        val currentUser = userViewModel.getCurrentUser()
        userId = currentUser?.uid ?: ""

        shoppingItemViewModel = ShoppingItemViewModel(shoppingRepositoryImpl())

        shoppingItemViewModel.itemsList.observe(viewLifecycleOwner, { updatedItems ->
            shoppingItemAdapter.updateItems(updatedItems)  // Notify the adapter with updated items
        })

        shoppingItemAdapter = ShoppingItemAdapter(emptyList(), object : ShoppingItemAdapter.OnItemClickListener {
            override fun onEditClick(item: ShoppingItem) {
                Toast.makeText(requireContext(), "Edit button clicked for ${item.name}", Toast.LENGTH_SHORT).show()

                val bundle = Bundle().apply {
                    putString("itemID", item.id)
                    putString("itemName", item.name)
                    putString("itemQuantity", item.quantity.toString())
                    putString("itemPrice", item.price.toString())
                }

                val editFragment = EditShoppingItemFragment().apply {
                    arguments = bundle
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, editFragment)
                    .addToBackStack(null)
                    .commit()
            }

            override fun onDeleteClick(item: ShoppingItem) {
                Toast.makeText(requireContext(), "Delete button clicked for ${item.name}", Toast.LENGTH_SHORT).show()
                shoppingItemViewModel.deleteItem(userId, item.id)
            }

            override fun onFavoriteToggle(item: ShoppingItem, isFavorite: Boolean) {
                Toast.makeText(requireContext(), "Toggle button clicked for ${item.name}", Toast.LENGTH_SHORT).show()
                Log.d("fedf",isFavorite.toString())
                shoppingItemViewModel.toggleFavorite(userId, item.id, isFavorite)
            }
        })



        recyclerView.adapter = shoppingItemAdapter

        // Observe the shopping items from the ViewModel
        shoppingItemViewModel.itemsList.observe(viewLifecycleOwner, Observer { items ->
            // Update the adapter with the new list of items
            items.forEach { item ->
                Log.d("ItemFavoriteStatus", "Item ID: ${item.id}, Favorite: ${item.favorite}")
            }
            shoppingItemAdapter.updateItems(items) // This will update the RecyclerView with the new data
        })

        shoppingItemViewModel.operationStatus.observe(viewLifecycleOwner, Observer { status ->
            if (status.isNotEmpty()) {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch the shopping items for the user
        shoppingItemViewModel.getItems(userId)

        return view
    }
}
