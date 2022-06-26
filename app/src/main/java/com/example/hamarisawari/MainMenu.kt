package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        replaceFragment(HomeFragment())


        var searchVh: ImageView = findViewById(R.id.imgSearch)
        var rentVh: ImageView = findViewById(R.id.imgRent)
        var profile: ImageView = findViewById(R.id.imgProfile)
        var home: ImageView = findViewById(R.id.imgHome)


        profile.setOnClickListener {
            replaceFragment(ProfileFragment())
        }
        searchVh.setOnClickListener {
            replaceFragment(SearchFragment())
        }
        home.setOnClickListener {
            replaceFragment(HomeFragment())
        }
        rentVh.setOnClickListener {
            replaceFragment(RentFragment())
        }

    }

    private fun replaceFragment(fragment : androidx.fragment.app.Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFragment, fragment)
        fragmentTransaction.commit()

    }
}