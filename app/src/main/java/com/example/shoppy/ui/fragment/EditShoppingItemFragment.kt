package com.example.shoppy.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.shoppy.R
import com.example.shoppy.databinding.FragmentEditShoppingItemBinding
import com.example.shoppy.repository.shoppingRepositoryImpl
import com.example.shoppy.respository.UserRepositoryImp
import com.example.shoppy.viewmodel.ShoppingItemViewModel
import com.example.week3.viewmodel.UserViewModel

class EditShoppingItemFragment : Fragment() {

    private var _binding: FragmentEditShoppingItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var shoppingItemViewModel: ShoppingItemViewModel

    private lateinit var userViewModel: UserViewModel
    private lateinit var userId: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the binding layout and get the binding object
        _binding = FragmentEditShoppingItemBinding.inflate(inflater, container, false)
        val view = binding.root

        // Retrieve the arguments (the data passed from HomeFragment)
        val bundle = arguments
            val itemID = bundle?.getString("itemID")
            val itemName = bundle?.getString("itemName")
            val itemQuantity = bundle?.getString("itemQuantity")
            val itemPrice = bundle?.getString("itemPrice")
            Log.d("Mef","item:$itemName,$itemQuantity,$itemPrice")
            // Set the retrieved data to the respective fields
            binding.itemNameEditText.setText(itemName)
            binding.itemQuantityEditText.setText(itemQuantity)
            binding.itemPriceEditText.setText(itemPrice)

        userViewModel = UserViewModel(UserRepositoryImp())

        val currentUser = userViewModel.getCurrentUser()
        userId = currentUser?.uid ?: ""
        shoppingItemViewModel = ShoppingItemViewModel(shoppingRepositoryImpl())

//        shoppingItemViewModel.operationStatus.observe(viewLifecycleOwner, Observer { status ->
//            // Update UI based on the operation status
//            if (status.contains("successfully")) {
//                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireContext(), status, Toast.LENGTH_LONG).show()
//            }
//        }
        // Save button click listener
        binding.saveItemButton.setOnClickListener {
            val updatedItemName = binding.itemNameEditText.text.toString()
            val updatedQuantity = binding.itemQuantityEditText.text.toString().toIntOrNull() ?: 0 // Convert to Integer
            val updatedPrice = binding.itemPriceEditText.text.toString().toDoubleOrNull() ?: 0.0 // Convert to Double
            val data = mutableMapOf<String, Any>(
                "name" to updatedItemName,
                "quantity" to updatedQuantity,
                "price" to updatedPrice
            )
            // Call your ViewModel to update the item
            if (itemID != null) {
                shoppingItemViewModel.updateItem(userId, itemID, data)
            }

            // Optionally show a toast or navigate back
            Toast.makeText(requireContext(), "Item updated!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack() // Go back to the HomeFragment
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks by clearing the binding reference
    }
}
