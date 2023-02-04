package com.example.hamarisawari

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.Fragments.HomeFragment
import com.example.hamarisawari.Fragments.ProfileFragment
import com.example.hamarisawari.Fragments.RentFragment
import com.example.hamarisawari.Fragments.SearchFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject


class MainMenu : AppCompatActivity() {

    private val TAG = "PushNotification"

    lateinit var username: String
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var currentLocation: Location
    private val permissionCode = 101
    lateinit var dataEditor : Editor
    lateinit var myToken: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        //fetching details -> username/fragment status etc

        var mySharedPref: SharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
        username = mySharedPref.getString("username", null).toString()
        var currentFragment = mySharedPref.getString("fragmentStatus", "homeFragment").toString()
        dataEditor = mySharedPref.edit()

        checkFragmentStatus(currentFragment)
        //fetching user's current location to store it into database.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocationUser()

        //fetching & storing the device's notification token so we can send notification
        //to the device later on.
        getToken()




        //Log.d("Fragment Status: ", currentFragment.toString())

        val drawerlayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)




        toggle = ActionBarDrawerToggle(this, drawerlayout, R.string.open, R.string.close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            it.isChecked = true
            when(it.itemId){

                R.id.nav_viewBookings -> {

                    startActivity(Intent(this, ViewMyBookings::class.java))
                }

                R.id.nav_complaints -> {
                    startActivity(Intent(this, ProblemReport::class.java))
                }

                R.id.nav_logout -> {
                    dataEditor.putBoolean("loggedIn", false)
                    dataEditor.putString("screenNavigation", "MainMenu")
                    dataEditor.apply()
                    dataEditor.commit()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

            }

            true
        }




        var searchVh: ImageView = findViewById(R.id.imgSearch)
        var rentVh: ImageView = findViewById(R.id.imgRent)
        var profile: ImageView = findViewById(R.id.imgProfile)
        var home: ImageView = findViewById(R.id.imgHome)

        profile.setColorFilter(Color.parseColor("#000000"))
        searchVh.setColorFilter(Color.parseColor("#000000"))
        home.setColorFilter(Color.parseColor("#8E0707"))
        rentVh.setColorFilter(Color.parseColor("#000000"))

        profile.setOnClickListener {

            dataEditor.putString("fragmentStatus", "profileFragment")
            dataEditor.apply()
            dataEditor.commit()
            profile.setColorFilter(Color.parseColor("#8E0707"))
            searchVh.setColorFilter(Color.parseColor("#000000"))
            home.setColorFilter(Color.parseColor("#000000"))
            rentVh.setColorFilter(Color.parseColor("#000000"))
            replaceFragment(ProfileFragment())

        }
        searchVh.setOnClickListener {

            dataEditor.putString("fragmentStatus", "searchFragment")
            dataEditor.apply()
            dataEditor.commit()

            profile.setColorFilter(Color.parseColor("#000000"))
            searchVh.setColorFilter(Color.parseColor("#8E0707"))
            home.setColorFilter(Color.parseColor("#000000"))
            rentVh.setColorFilter(Color.parseColor("#000000"))
            replaceFragment(SearchFragment())
        }
        home.setOnClickListener {

            dataEditor.putString("fragmentStatus", "homeFragment")
            dataEditor.apply()
            dataEditor.commit()
            profile.setColorFilter(Color.parseColor("#000000"))
            searchVh.setColorFilter(Color.parseColor("#000000"))
            home.setColorFilter(Color.parseColor("#8E0707"))
            rentVh.setColorFilter(Color.parseColor("#000000"))
            replaceFragment(HomeFragment())
        }
        rentVh.setOnClickListener {

            dataEditor.putString("fragmentStatus", "rentFragment")
            dataEditor.apply()
            dataEditor.commit()
            profile.setColorFilter(Color.parseColor("#000000"))
            searchVh.setColorFilter(Color.parseColor("#000000"))
            home.setColorFilter(Color.parseColor("#000000"))
            rentVh.setColorFilter(Color.parseColor("#8E0707"))
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


    private fun getCurrentLocationUser() {

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        val getLocation = fusedLocationProviderClient.lastLocation.addOnSuccessListener {

                location ->

            if(location != null){
                currentLocation = location

                Log.d("My Current Lat", currentLocation.latitude.toString())
                Log.d("My Current Lon", currentLocation.longitude.toString())
                //after fetching user's location we save it for later use.
                dataEditor.putString("latitude", currentLocation.latitude.toString())
                dataEditor.putString("longitude", currentLocation.longitude.toString())
                dataEditor.apply()
                dataEditor.commit()
                updateUserLocationAndToken()
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            permissionCode -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocationUser()
            }
        }

    }
    private fun updateUserLocationAndToken(){


        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().updateLocationAndToken_URL,
            Response.Listener { response ->


            },
            Response.ErrorListener { error ->

                Toast.makeText(this@MainMenu, error.toString(), Toast.LENGTH_SHORT).show()
                Log.d("my location: ", error.toString())
            }){

            override fun getParams(): Map<String, String> {


                val map : MutableMap<String,String> = HashMap()

                map["username"] = username
                map["latitude"] = currentLocation.latitude.toString()
                map["longitude"] = currentLocation.longitude.toString()
                map["token"] = myToken
                return map
            }
        }

        val queue = Volley.newRequestQueue(this@MainMenu)
        queue.add(request)
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
                task ->

            if(!task.isSuccessful){
                Log.d(TAG, "Token Creation Failed" )
            }
            else{
                myToken = task.getResult()
                Log.d(TAG, myToken)
            }
        })
    }

}