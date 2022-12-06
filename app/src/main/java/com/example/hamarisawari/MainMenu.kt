package com.example.hamarisawari

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hamarisawari.Fragments.*
import com.google.android.material.navigation.NavigationView


class MainMenu : AppCompatActivity(), Communicator {


     lateinit var toggle: ActionBarDrawerToggle



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        var mySharedPref: SharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
        var currentFragment = mySharedPref.getString("fragmentStatus", "homeFragment").toString()
        var dataEditor = mySharedPref.edit()

        checkFragmentStatus(currentFragment)


        Log.d("Fragment Status: ", currentFragment.toString())

        val drawerlayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)




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




        var searchVh: ImageView = findViewById(R.id.imgSearch)
        var rentVh: ImageView = findViewById(R.id.imgRent)
        var profile: ImageView = findViewById(R.id.imgProfile)
        var home: ImageView = findViewById(R.id.imgHome)


        profile.setOnClickListener {

            dataEditor.putString("fragmentStatus", "profileFragment")
            dataEditor.apply()
            dataEditor.commit()

            replaceFragment(ProfileFragment())

        }
        searchVh.setOnClickListener {

            dataEditor.putString("fragmentStatus", "searchFragment")
            dataEditor.apply()
            dataEditor.commit()

            replaceFragment(SearchFragment())
        }
        home.setOnClickListener {

            dataEditor.putString("fragmentStatus", "homeFragment")
            dataEditor.apply()
            dataEditor.commit()

            replaceFragment(HomeFragment())
        }
        rentVh.setOnClickListener {

            dataEditor.putString("fragmentStatus", "rentFragment")
            dataEditor.apply()
            dataEditor.commit()

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
    private fun checkFragmentStatus(currentFragment: String) {

        if(currentFragment == "homeFragment"){
            replaceFragment(HomeFragment())
        }
        else if(currentFragment == "rentFragment"){
            replaceFragment(RentFragment())
        }
        else if(currentFragment == "searchFragment"){
            replaceFragment(SearchFragment())
        }
        else if(currentFragment == "profileFragment"){
            replaceFragment(ProfileFragment())
        }
    }

    override fun passDataCom(images: ArrayList<String>) {

        val bundle = Bundle()
        bundle.putStringArrayList("images", images)
        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentCar = RentCarFragment()
        fragmentCar.arguments = bundle
        transaction.replace(R.id.mainFragment, fragmentCar)
        transaction.commit()
    }


}