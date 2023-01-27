package com.example.hamarisawari

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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

class ContactAndCommunications : AppCompatActivity() {


    lateinit var googleMap: GoogleMap
    lateinit var username: String
    private val CHANNEL_ID = "101"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_and_communications)

        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        var myLatitude = mySharedPref.getString("latitude", null).toString()
        var myLongitude = mySharedPref.getString("longitude", null).toString()
        username = mySharedPref.getString("username", null).toString()

        val bundle = intent.extras
        var renterLatitude = bundle?.getString("latitude").toString()
        var renterLongitude = bundle?.getString("longitude").toString()
        var renterUsername = bundle?.getString("username").toString()


        //this is the menu for selecting days of rent
        displayDropDown()

        //initializing map and setting a mark for the users & vehicle's location.
        initializeMap(myLatitude, myLongitude, renterLatitude, renterLongitude)


        createNotificationChannel()
        sendNotificationToRenter(renterUsername)

        var bookingButton = findViewById<Button>(R.id.bookVehicle)
        bookingButton.setOnClickListener {

            var rentingDays = findViewById<AutoCompleteTextView>(R.id.rentDays).text.toString()
            var rentingHours = findViewById<AutoCompleteTextView>(R.id.rentHours).text.toString()
            var rentingMinutes = findViewById<AutoCompleteTextView>(R.id.rentMinutes).text.toString()

            MaterialAlertDialogBuilder(this)
                .setTitle("Warning")
                .setMessage("You are sending a request to rent a vehicle for $rentingDays Days $rentingHours Hours AND $rentingMinutes Minutes." +
                        "Once the owner accepts your request your timer will start immediately. Do you wish to send request?")
                .setNegativeButton("No"){
                        dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton("Yes"){
                        dialog, which ->


//                    val nDialog: ProgressDialog
//                    nDialog = ProgressDialog(this)
//                    nDialog.setMessage("Please wait while the user responds to your request.")
//                    nDialog.setTitle("Waiting for response")
//                    nDialog.isIndeterminate = false
//                    nDialog.setCancelable(true)
//                    nDialog.show()
                startActivity(Intent(this, CurrentlyActiveBooking::class.java))
                }
                .show()

            sendBookingRequest()


        }


    }

    private fun sendBookingRequest() {

    }

    private fun initializeMap(
        myLatitude: String,
        myLongitude: String,
        renterLatitude: String,
        renterLongitude: String
    ) {

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            val myLocation = LatLng(myLatitude.toDouble(), myLongitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12.0f))

            val renterLocation = LatLng(renterLatitude.toDouble(), renterLongitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(renterLocation).title("Renter Location"))
        })
    }

    private fun sendNotificationToRenter(renterUsername: String) {


        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().notifyRenter_URL,
            Response.Listener { response ->

                Toast.makeText(this, "Notification Sent", Toast.LENGTH_LONG).show()

                //Log.d("My Response:", response.toString() )
            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()

                Log.d("My Error:", error.toString() )
            }){

            override fun getParams(): Map<String, String> {

                val map : MutableMap<String,String> = HashMap()
                map["renterUsername"] = renterUsername

                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Vehicle Booking Confirmation"
            val descriptionText = "Someone wants to book your vehicle"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun displayDropDown()
    {
        val days = resources.getStringArray(R.array.Days)
        val hours = resources.getStringArray(R.array.Hours)
        val minutes = resources.getStringArray(R.array.Minutes)

        val arrayAdapterDays = ArrayAdapter(this@ContactAndCommunications, R.layout.dropdown_menu, days)
        val arrayAdapterHours = ArrayAdapter(this@ContactAndCommunications, R.layout.dropdown_menu, hours)
        val arrayAdapterMinutes = ArrayAdapter(this@ContactAndCommunications, R.layout.dropdown_menu, minutes)



        findViewById<AutoCompleteTextView>(R.id.rentDays).setAdapter(arrayAdapterDays)
        findViewById<AutoCompleteTextView>(R.id.rentHours).setAdapter(arrayAdapterHours)
        findViewById<AutoCompleteTextView>(R.id.rentMinutes).setAdapter(arrayAdapterMinutes)
    }
}