package com.example.shoppy.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.shoppy.R
import com.example.shoppy.databinding.ActivityBottomNavActitvityBinding
import com.example.shoppy.ui.fragment.AddShoppingItemFragment
import com.example.shoppy.ui.fragment.HomeFragment
import com.example.shoppy.ui.fragment.favoriteFragment
import com.example.shoppy.ui.fragment.profileFragment

class bottomNavActivity : AppCompatActivity() {



    lateinit var navigationBinding: ActivityBottomNavActitvityBinding

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        navigationBinding = ActivityBottomNavActitvityBinding.inflate(layoutInflater)

        setContentView(navigationBinding.root)

        replaceFragment(HomeFragment())
        navigationBinding.buttomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> replaceFragment(HomeFragment())
                R.id.menuAdditem ->replaceFragment(AddShoppingItemFragment())
                R.id.menuFavourite ->replaceFragment(favoriteFragment())
                R.id.menuProfile -> replaceFragment(profileFragment())
                else -> {}
            }
            true
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

}