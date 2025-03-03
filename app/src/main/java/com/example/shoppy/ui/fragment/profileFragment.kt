package com.example.shoppy.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.shoppy.R
import com.example.shoppy.databinding.FragmentProfileBinding
import com.example.shoppy.model.UserModel
import com.example.shoppy.respository.UserRepositoryImp
import com.example.shoppy.services.CloudinaryService
import com.example.week3.viewmodel.UserViewModel

class profileFragment : Fragment(), CloudinaryService.ImageUploadCallback {

    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var cloudinaryService: CloudinaryService

    private val PICK_IMAGE_REQUEST = 71
    private var profileImageUri: Uri? = null
    lateinit var userId: String
    private var uploadedImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        // Ensure CloudinaryService is initialized
        cloudinaryService = CloudinaryService.getInstance(requireContext())

        // Initialize ViewModel
        userViewModel = UserViewModel(UserRepositoryImp())

        // Get current user details
        val currentUser = userViewModel.getCurrentUser()
        userId = currentUser?.uid ?: ""
        Log.d("id", "UserId $userId")
        userViewModel.getUserFromDatabase(userId)

        userViewModel._userData.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                Log.d("UserData", "User data received: $it")

                // Populate the UI fields with user data
                binding.firstNameEditText.setText(user.firstname)
                binding.lastNameEditText.setText(user.lastname)
                binding.addressEditText.setText(user.address)
                binding.phoneNumberEditText.setText(user.phonenumber)
                binding.emailEditText.setText(user.email)

                // Corrected: Use profileUrl instead of user.profilePicture
                val profileUrl = if (!user.profilePicture.isNullOrEmpty()) {
                    user.profilePicture
                } else {
                    "https://ui-avatars.com/api/?name=${user.firstname}+${user.lastname}&background=random"
                }

                Glide.with(requireContext())
                    .load(profileUrl) // Use profileUrl instead
                    .circleCrop()
                    .into(binding.profileImageView)

                Log.d("msg", "Profile pic URL: $profileUrl")

            } ?: run {
                Log.d("UserData", "No user data available")
            }
        })


        // Handle Change Profile Picture Button
        binding.changeProfilePictureButton.setOnClickListener {
            openImageChooser()
        }

        // Handle Save Profile Button
        binding.saveProfileButton.setOnClickListener {
            if (profileImageUri != null) {
                // Upload the image to Cloudinary
                cloudinaryService.uploadImage(profileImageUri!!, this)
            } else {
                // Save without an image if not selected
                saveProfile("") // If no image selected, pass empty URL
            }
        }

        return view
    }

    // Open Image Chooser to select profile picture
    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handle result of image chooser
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            profileImageUri = data.data
            binding.profileImageView.setImageURI(profileImageUri)
        }
    }

    // Clear binding when view is destroyed to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Implement Cloudinary upload callbacks
    override fun onUploadStart() {
        // Show a loading indicator if needed
    }

    override fun onUploadProgress(bytes: Long, totalBytes: Long) {
        // Optionally, show upload progress
    }

    override fun onUploadSuccess(imageUrl: String) {
        Log.d("msg", "Upload successful! URL: $imageUrl")
        uploadedImageUrl = imageUrl  // Store the uploaded image URL

        // Replace http:// with https:// for secure URL
        val secureImageUrl = if (imageUrl.startsWith("http://")) {
            imageUrl.replace("http://", "https://")
        } else {
            imageUrl
        }

        // Now save the profile with the uploaded image URL
        saveProfile(secureImageUrl)
    }

    override fun onUploadRescheduled(errorDescription: String) {
        // Handle rescheduled uploads if needed
    }

    override fun onUploadError(errorDescription: String) {
        Toast.makeText(requireContext(), "Error uploading image: $errorDescription", Toast.LENGTH_SHORT).show()
    }

    // Function to save profile to database
    private fun saveProfile(imageUrl: String) {
        val model = UserModel(
            firstname = binding.firstNameEditText.text.toString(),
            lastname = binding.lastNameEditText.text.toString(),
            address = binding.addressEditText.text.toString(),
            phonenumber = binding.phoneNumberEditText.text.toString(),
            email = binding.emailEditText.text.toString(),
            profilePicture = imageUrl  // Save profile picture URL or empty string
        )

        // Convert UserModel to MutableMap<String, Any>
        val dataMap: MutableMap<String, String?> = mutableMapOf(
            "firstname" to model.firstname,
            "lastname" to model.lastname,
            "address" to model.address,
            "phonenumber" to model.phonenumber,
            "email" to model.email,
            "profilePicture" to model.profilePicture
        )

        // Use the converted map in the updateProfile function
        if (userId.isNotEmpty()) {
            userViewModel.updateProfile(userId, dataMap)
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }
    }
}
