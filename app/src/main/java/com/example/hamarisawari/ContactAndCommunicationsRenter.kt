package com.example.hamarisawari

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ContactAndCommunicationsRenter : AppCompatActivity() {

    lateinit var googleMap: GoogleMap
    lateinit var username: String

    lateinit var cancelBooking: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_and_communications_renter)


        cancelBooking = findViewById(R.id.cancelBookingRenter)

        //fetching users location and username.
        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        var myLatitude = mySharedPref.getString("latitude", null).toString()
        var myLongitude = mySharedPref.getString("longitude", null).toString()
        username = mySharedPref.getString("username", null).toString()

        //data variables to be fetched from the user sending notification
        var rentee: String = ""
        var renter: String = ""
        var VehicleType: String = ""
        var Numberplate: String = ""

        //Log.d("ClickActionActivity", "Activity Launched")
        val extras = intent.extras
        //val extras = intent.getBundleExtra("AA")
        //Log.d("Extras", extras.toString())
        if (extras != null) {
            Log.d("rentee", extras.getString("rentee").toString())
            Log.d("renter", extras.getString("renter").toString())
            Log.d("VehicleType", extras.getString("VehicleType").toString())
            Log.d("Numberplate", extras.getString("Numberplate").toString())

            rentee = extras.getString("rentee").toString()
            renter = extras.getString("renter").toString()
            VehicleType = extras.getString("VehicleType").toString()
            Numberplate = extras.getString("Numberplate").toString()
            initializeBooking(renter, rentee, VehicleType, Numberplate)
        }
        else{
            Toast.makeText(this, "Booking could not be initialized, there must be some error in user request.", Toast.LENGTH_LONG).show()
        }

        //initializing map and setting a mark for the users & vehicle's location.
        initializeMap(myLatitude, myLongitude)


        cancelBooking.setOnClickListener {

            MaterialAlertDialogBuilder(this)
                .setTitle("WARNING!")
                .setMessage(
                    "Are you sure you want to cancel renting out your vehicle to the user?"
                )
                .setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton("Yes") { dialog, which ->
                    cancelBooking(renter, rentee, VehicleType, Numberplate)
                    startActivity(Intent(this, MainMenu::class.java))
                    finish()
                }
                .show()


        }

    }

    private fun initializeBooking(myUsername: String, renteeUsername: String, typE: String, numberPlate: String) {

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().initializeBooking_URL,
            Response.Listener { response ->

                Toast.makeText(this, response, Toast.LENGTH_LONG).show()

                //Log.d("My Response:", response.toString() )
            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()

                Log.d("My Error:", error.toString() )
            }){

            override fun getParams(): Map<String, String> {

                val map : MutableMap<String,String> = HashMap()
                map["renterUsername"] = myUsername
                map["renteeUsername"] = renteeUsername
                map["type"] = typE
                map["numberPlate"] = numberPlate

                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }


    private fun initializeMap(myLatitude: String, myLongitude: String) {

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            val myLocation = LatLng(myLatitude.toDouble(), myLongitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f))

            //val renterLocation = LatLng(renterLatitude.toDouble(), renterLongitude.toDouble())
            //googleMap.addMarker(MarkerOptions().position(renterLocation).title("Renter Location"))
        })
    }
    private fun cancelBooking(myUsername: String, renteeUsername: String, typE: String, numberPlate: String) {

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().cancelBooking_URL,
            Response.Listener { response ->

                Toast.makeText(this, response, Toast.LENGTH_LONG).show()

                //Log.d("My Response:", response.toString() )
            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()

                Log.d("My Error:", error.toString() )
            }){

            override fun getParams(): Map<String, String> {

                val map : MutableMap<String,String> = HashMap()
                map["renterUsername"] = myUsername
                map["renteeUsername"] = renteeUsername
                map["type"] = typE
                map["numberPlate"] = numberPlate

                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}