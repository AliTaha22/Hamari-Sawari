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
import java.util.*
import kotlin.collections.HashMap

class ContactAndCommunications : AppCompatActivity() {


    lateinit var googleMap: GoogleMap
    lateinit var username: String
    private val CHANNEL_ID = "102"

    lateinit var rentingDays: String
    lateinit var rentingHours: String
    lateinit var rentingMinutes: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_and_communications)

        //fetching user and owners details to put it on screen.
        //<<--------------------------------------------------------------------------------
        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        username = mySharedPref.getString("username", null).toString()
        var myLatitude = mySharedPref.getString("latitude", null).toString()
        var myLongitude = mySharedPref.getString("longitude", null).toString()

        val bundle = intent.extras
        var renterLatitude = bundle?.getString("latitude").toString()
        var renterLongitude = bundle?.getString("longitude").toString()
        var renterUsername = bundle?.getString("username").toString()
        var renterVhType = bundle?.getString("vhtype").toString()
        var renterVhNumberplate = bundle?.getString("vhnumberplate").toString()
        //-------------------------------------------------------------------------------->>


        //this is the menu for selecting days of rent
        displayDropDown()

        //initializing map and setting a mark for the users & vehicle's location.
        initializeMap(myLatitude, myLongitude, renterLatitude, renterLongitude)


        var bookingButton = findViewById<Button>(R.id.bookVehicle)
        bookingButton.setOnClickListener {

            rentingDays = findViewById<AutoCompleteTextView>(R.id.rentDays).text.toString()
            rentingHours = findViewById<AutoCompleteTextView>(R.id.rentHours).text.toString()
            rentingMinutes = findViewById<AutoCompleteTextView>(R.id.rentMinutes).text.toString()

            MaterialAlertDialogBuilder(this)
                .setTitle("IMPORTANT!")
                .setMessage(
                    "You are sending a request to rent a vehicle for $rentingDays Days $rentingHours Hours AND $rentingMinutes Minutes." +
                            "Once the owner accepts your request your timer will start immediately. Do you wish to send request?"
                )
                .setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton("Yes") { dialog, which ->

                    //if the user agrees, a confirmation notification will be sent to Owner of vehicle.
                    createNotificationChannel()
                    sendNotificationToRenter(renterUsername,renterVhType, renterVhNumberplate )

                    //user waits until the owner responds to the notification.
                    val nDialog: ProgressDialog
                    nDialog = ProgressDialog(this)
                    nDialog.setMessage("Please wait while the user responds to your request.")
                    nDialog.setTitle("Waiting for response")
                    nDialog.isIndeterminate = false
                    nDialog.setCancelable(true)
                    nDialog.show()

                    Log.d("MAIN", "BEFORE CALLING FUNCTION")
                    checkOwnerResponse(username, renterUsername)


                }
                .show()
        }

        var cancelBookingButton = findViewById<Button>(R.id.cancelBooking)
        cancelBookingButton.setOnClickListener {

            startActivity(Intent(this, MainMenu::class.java))
            finish()

        }

    }


    private fun checkOwnerResponse(myUsername: String, renterUsername: String){

        Log.d("Inside Function: ", "HELLO")
        var previousData = "Initialized"
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                val request: StringRequest = object : StringRequest(
                    Method.POST, URLs().checkOwnerResponse_URL,
                    Response.Listener { response ->

                        Log.d("Response: ", response.toString())
                        if(response.contains("Booked")){
                            runOnUiThread{
                                bookingConfirmed()
                            }
                            timer.cancel()
                        }
                        else if(response.contains("Cancelled")){
                            runOnUiThread{
                                bookingCancelled()
                            }
                            timer.cancel()

                        }
                        /*Log.d("Response: ", response.toString())
                        // Parse the response and extract the data
                        val latestData = response
                        Log.d("Latest Data: ", latestData.toString())
                        if (latestData != previousData) {

                            Log.d("Updated: ", latestData.toString())
                            // Update UI on main thread
                            runOnUiThread {
                                if(response.contains("Booked")){
                                    bookingConfirmed()
                                }
                                else if(latestData.contains("Cancelled")){
                                    bookingCancelled()
                                }
                            }
                            previousData = latestData
                        }*/
                    },
                    Response.ErrorListener { error ->

                        Toast.makeText(this@ContactAndCommunications, error.toString(), Toast.LENGTH_LONG).show()

                        Log.d("My Error:", error.toString())
                    }) {

                    override fun getParams(): Map<String, String> {

                        val map: MutableMap<String, String> = HashMap()
                        map["myUsername"] = myUsername
                        map["renterUsername"] = renterUsername

                        return map
                    }
                }
                val queue = Volley.newRequestQueue(this@ContactAndCommunications)
                queue.add(request)
            }
        }
        timer.schedule(task, 0, 2000)

    }

    private fun initializeMap(myLatitude: String, myLongitude: String, renterLatitude: String, renterLongitude: String) {

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

    private fun sendNotificationToRenter(renterUsername: String, renterVhType: String,renterVhNumberplate: String) {

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().requestBooking_URL,
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
                map["renterUsername"] = renterUsername
                map["renteeUsername"] = username
                map["days"] = rentingDays
                map["hours"] = rentingHours
                map["minutes"] = rentingMinutes
                map["type"] = renterVhType
                map["numberplate"] = renterVhNumberplate

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
            val descriptionText = "Confirm or not?"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel("102", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun bookingCancelled() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Request Cancelled")
            .setMessage(
                "Unfortunately the owner of vehicle has cancelled your booking request. Click OK to continue."
            )
            .setNeutralButton("OK"){dialog, which ->

                startActivity(Intent(this, MainMenu::class.java))
                finish()

            }.show()
    }

    private fun bookingConfirmed() {

        val i = Intent(this, CurrentlyActiveBooking::class.java)

        val bundle = Bundle()
        bundle.putString("days", rentingDays)
        bundle.putString("hours", rentingHours)
        bundle.putString("minutes", rentingMinutes)


        i.putExtras(bundle)
        startActivity(i)
        finish()
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