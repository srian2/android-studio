package com.example.shoppy.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shoppy.R
import com.example.shoppy.databinding.ActivityRegisterBinding
import com.example.shoppy.model.UserModel
import com.example.shoppy.respository.UserRepositoryImp
import com.example.shoppy.utils.LoadingUtils
import com.example.week3.viewmodel.UserViewModel


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private  lateinit var userViewModel : UserViewModel
    lateinit var loadingUtils: LoadingUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val repo = UserRepositoryImp()
        userViewModel = UserViewModel(repo)

        binding.btnRegister.setOnClickListener {
            loadingUtils.show()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val fname = binding.firstname.text.toString()
            val address = binding.address.text.toString()
            val lname = binding.lastname.text.toString()
            val phone = binding.phone.text.toString()


            userViewModel.register(email,password){
                success,message,userID ->
                Log.d("RegisterActivity", "Register callback invoked. Success: $success, Message: $message")

                if(success){
                    Log.d("RegisterActivity", "UserID: $userID. Calling addUser...")

                    val usermodel = UserModel(userID.toString(),fname,lname,address,phone,email,password)
                    addUser(usermodel)
                        loadingUtils.dismiss()
                    }else{
                        loadingUtils.dismiss()
                    Log.e("RegisterActivity", "Registration failed: $message")


                    Toast.makeText(this@RegisterActivity,message,Toast.LENGTH_SHORT).show()
                    }

            }



        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addUser(userModel: UserModel) {

        userModel.userid?.let {
            userViewModel.addUserToDatabase(it, userModel) { success, message ->
                Log.d("RegisterActivity from add user", "AddUser callback invoked. Success: $success, Message: $message")


                if (success) {
                    Log.d("RegisterActivity", "user registered successfull")

                    Toast.makeText(
                        this@RegisterActivity,
                        message, Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.d("error from adduser",message)
                    Toast.makeText(
                        this@RegisterActivity,
                        message, Toast.LENGTH_LONG
                    ).show()

                }
                loadingUtils.dismiss()

            }
        }
    }}
