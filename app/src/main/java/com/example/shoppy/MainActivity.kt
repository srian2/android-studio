package com.example.shoppy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.shoppy.ui.activity.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        // Load the GIF using Glide
        val splashGif = findViewById<ImageView>(R.id.splashGif)
        Glide.with(this)
            .asGif() // Important to specify it's a GIF
            .load(R.raw.last) // Ensure the GIF is placed in `res/raw/`
            .into(splashGif)

        // Wait for 2 seconds before launching the main activity
        Handler().postDelayed({
            // Start the main activity
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish() // Close splash activity
        }, 2000) // 2000 milliseconds = 2 seconds

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}