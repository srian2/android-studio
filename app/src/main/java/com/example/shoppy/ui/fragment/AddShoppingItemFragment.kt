package com.example.shoppy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shoppy.databinding.FragmentAddShoppingItemBinding
import com.example.shoppy.model.ShoppingItem
import com.example.shoppy.repository.shoppingRepositoryImpl
import com.example.shoppy.respository.UserRepositoryImp
import com.example.shoppy.viewmodel.ShoppingItemViewModel
import com.example.week3.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddShoppingItemFragment : Fragment() {

    private lateinit var shoppingItemViewModel: ShoppingItemViewModel
    private lateinit var userViewModel: UserViewModel
    private var userId: String = ""

    private var _binding: FragmentAddShoppingItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using View Binding
        _binding = FragmentAddShoppingItemBinding.inflate(inflater, container, false)
        val view = binding.root

        // Get current user ID

        userViewModel = UserViewModel(UserRepositoryImp())
        // Get current user ID
        val currentUser = userViewModel.getCurrentUser()
        userId = currentUser?.uid ?: ""
        // Initialize ViewModel
        shoppingItemViewModel = ShoppingItemViewModel(shoppingRepositoryImpl())
        // Observe the operationStatus LiveData

        shoppingItemViewModel.operationStatus.observe(viewLifecycleOwner, Observer { status ->
            // Show the status message (success or error) using a Toast
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
        })
        // Set OnClickListener for Save button using binding
        binding.saveItemButton.setOnClickListener {
            saveShoppingItem()
        }

        return view
    }

    private fun saveShoppingItem() {
        val itemName = binding.itemNameEditText.text.toString()
        val itemQuantity = binding.itemQuantityEditText.text.toString()
        val itemPrice = binding.itemPriceEditText.text.toString()

        // Validate input
        if (itemName.isNotEmpty() && itemQuantity.isNotEmpty() && itemPrice.isNotEmpty()) {
            val shoppingItem = ShoppingItem(
                name = itemName,
                quantity = itemQuantity.toInt(),
                price = itemPrice.toLong(),
            )

            // Call ViewModel to save shopping item
            shoppingItemViewModel.addItem(userId, shoppingItem)
        } else {
            // Show error message if fields are empty
            Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
        }
    }

    // Clean up the binding when the fragment is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
