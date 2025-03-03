package com.example.shoppy.ui.activity
import android.content.Intent

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shoppy.R
import com.example.shoppy.databinding.ActivityLoginBinding
import com.example.shoppy.respository.UserRepositoryImp
import com.example.week3.viewmodel.UserViewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userrepositoryimp = UserRepositoryImp()
        userViewModel= UserViewModel(userrepositoryimp)

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            var email =binding.etUsername.text.toString()
            var password = binding.etPassword.text.toString()
            userViewModel.login(email,password){success,message ->
                if(success){
                    Toast.makeText(this,"Login successfull",Toast.LENGTH_LONG).show()

                    val intent = Intent(this,bottomNavActivity::class.java)
                    startActivity(intent)
                    finish()
                    }else{
                    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
                }

            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}