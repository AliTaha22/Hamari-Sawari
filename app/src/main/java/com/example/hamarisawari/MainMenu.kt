package com.example.hamarisawari

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hamarisawari.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView


class MainMenu : AppCompatActivity() {


     lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)



        val drawerlayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        var mySharedPref = getSharedPreferences("userStatus", AppCompatActivity.MODE_PRIVATE)
        var dataEditor = mySharedPref.edit()


        toggle = ActionBarDrawerToggle(this, drawerlayout, R.string.open, R.string.close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            it.isChecked = true
            when(it.itemId){

                R.id.nav_addlocation -> Toast.makeText(this, "Clicked addLocation", Toast.LENGTH_LONG).show()

                R.id.nav_logout -> {
                    dataEditor.putBoolean("loggedIn", false)
                    dataEditor.putString("screenNavigation", "MainMenu")
                    dataEditor.apply()
                    dataEditor.commit()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }


                R.id.nav_message -> Toast.makeText(this, "Clicked message", Toast.LENGTH_LONG).show()
            }

            true
        }



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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    //function to switch between fragments.
    private fun replaceFragment(fragment : androidx.fragment.app.Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFragment, fragment)
        fragmentTransaction.commit()

    }

}