package com.example.hamarisawari

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
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
        var renteeLatitude: String = ""
        var renteeLongitude: String = ""
        var VhPrice: String = ""

        //Log.d("ClickActionActivity", "Activity Launched")
        val extras = intent.extras
        //val extras = intent.getBundleExtra("AA")
        //Log.d("Extras", extras.toString())

        var mySharedPref2: SharedPreferences = getSharedPreferences("BookingInfo", MODE_PRIVATE)
        var bookingStatus = mySharedPref2.getBoolean("Status", false)

        if (extras != null) {
            Log.d("rentee", extras.getString("rentee").toString())
            Log.d("renter", extras.getString("renter").toString())
            Log.d("VehicleType", extras.getString("VehicleType").toString())
            Log.d("Numberplate", extras.getString("Numberplate").toString())

            rentee = extras.getString("rentee").toString()
            renter = extras.getString("renter").toString()
            VehicleType = extras.getString("VehicleType").toString()
            Numberplate = extras.getString("Numberplate").toString()
            renteeLatitude = extras.getString("renteeLatitude").toString()
            renteeLongitude = extras.getString("renteeLongitude").toString()
            VhPrice = extras.getString("VhPrice").toString()

            if(bookingStatus == false){
                initializeBooking(renter, rentee, VehicleType, Numberplate, renteeLatitude, renteeLongitude, myLatitude,myLongitude, VhPrice  )
            }

        }
        else{
            Toast.makeText(this, "Booking could not be initialized, there must be some error in user request.", Toast.LENGTH_LONG).show()
        }

        //initializing map and setting a mark for the users & vehicle's location.
        initializeMap(myLatitude, myLongitude, renteeLatitude, renteeLongitude)
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


        var msg: Button = findViewById(R.id.messageRenter)
        msg.setOnClickListener {

            val i = Intent(this, Chating::class.java)
            var table = "$rentee$renter"
            val bundle = Bundle()
            bundle.putString("sender", renter)
            bundle.putString("receiver", rentee)
            bundle.putString("table", table)

            i.putExtras(bundle)
            startActivity(i)


        }

    }

    private fun initializeBooking(myUsername: String, renteeUsername: String, typE: String, numberPlate: String,
                                  renteeLatitude: String, renteeLongitude: String, renterLatitude: String, renterLongitude: String, VhPrice: String) {

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().initializeBooking_URL,
            Response.Listener { response ->

                Toast.makeText(this, response, Toast.LENGTH_LONG).show()

                if(response.contains("initialized")){

                    var mySharedPref: SharedPreferences = getSharedPreferences("BookingInfo", MODE_PRIVATE)
                    var dataEditor = mySharedPref.edit()
                    dataEditor.putBoolean("Status", true)
                    dataEditor.apply()
                    dataEditor.commit()
                }

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
                map["renteeLatitude"] = renteeLatitude
                map["renteLongitude"] = renteeLongitude
                map["renterLatitude"] = renterLatitude
                map["renterLongitude"] = renterLongitude
                map["VhPrice"] = VhPrice

                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }


    private fun initializeMap(myLatitude: String, myLongitude: String, renteeLatitude: String, renteeLongitude: String) {

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            val myLocation = LatLng(myLatitude.toDouble(), myLongitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f))

            val renteeLocation = LatLng(renteeLatitude.toDouble(), renteeLongitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(renteeLocation).title("Rentee Location"))
        })
    }
    private fun cancelBooking(myUsername: String, renteeUsername: String, typE: String, numberPlate: String) {

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().cancelBooking_URL,
            Response.Listener { response ->

                Toast.makeText(this, response, Toast.LENGTH_LONG).show()

                var mySharedPref: SharedPreferences = getSharedPreferences("BookingInfo", MODE_PRIVATE)
                var dataEditor = mySharedPref.edit()
                dataEditor.putBoolean("Status", false)
                dataEditor.apply()
                dataEditor.commit()

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