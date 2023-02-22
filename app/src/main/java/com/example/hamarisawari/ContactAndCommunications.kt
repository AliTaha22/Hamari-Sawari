package com.example.hamarisawari

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import android.Manifest
class ContactAndCommunications : AppCompatActivity() {


    lateinit var googleMap: GoogleMap
    lateinit var username: String

    lateinit var renterUsername:String
    lateinit var renterVhNumberplate:String
    lateinit var renterNum: String
    var totalPrice:Int = 0

    lateinit var priceTextView: TextView
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
        renterUsername = bundle?.getString("username").toString()
        var renterVhType = bundle?.getString("vhtype").toString()
        renterVhNumberplate = bundle?.getString("vhnumberplate").toString()
        var vehicleRentingPrice = bundle?.getString("vhprice").toString()
        renterNum = bundle?.getString("number").toString()
        //-------------------------------------------------------------------------------->>


        //accessing the id's of details on the screen
        val daysAutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.rentDays)
        val hoursAutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.rentHours)
        val minutesAutoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.rentMinutes)
        priceTextView = findViewById(R.id.totalPrice)

        calculatePrice(daysAutoCompleteTextView, hoursAutoCompleteTextView, minutesAutoCompleteTextView, vehicleRentingPrice.toInt())


        //this is the menu for selecting days of rent
        displayDropDown()

        //initializing map and setting a mark for the users & vehicle's location.
        initializeMap(myLatitude, myLongitude, renterLatitude, renterLongitude)


        var bookingButton = findViewById<Button>(R.id.bookVehicle)
        bookingButton.setOnClickListener {

            rentingDays = daysAutoCompleteTextView.text.toString()
            rentingHours = hoursAutoCompleteTextView.text.toString()
            rentingMinutes = minutesAutoCompleteTextView.text.toString()

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
        var msg: Button = findViewById(R.id.messageRenter)
        msg.setOnClickListener {

            val i = Intent(this, Chating::class.java)
            var table = "$username$renterUsername"
            val bundle = Bundle()
            bundle.putString("sender", username)
            bundle.putString("receiver", renterUsername)
            bundle.putString("table", table)

            i.putExtras(bundle)
            startActivity(i)


        }

        var call: Button = findViewById(R.id.callRenter)
        call.setOnClickListener {

            val dialIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$renterNum"))

// Check if the device supports phone calls
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // If the app doesn't have the CALL_PHONE permission, request it
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
            } else {
                // If the app already has the CALL_PHONE permission, start the phone call
                startActivity(dialIntent)
            }


        }

        var cancelBookingButton = findViewById<Button>(R.id.cancelBooking)
        cancelBookingButton.setOnClickListener {

            startActivity(Intent(this, MainMenu::class.java))
            finish()

        }

        daysAutoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculatePrice(daysAutoCompleteTextView, hoursAutoCompleteTextView, minutesAutoCompleteTextView, vehicleRentingPrice.toInt())
            }
        })

        hoursAutoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculatePrice(daysAutoCompleteTextView, hoursAutoCompleteTextView, minutesAutoCompleteTextView, vehicleRentingPrice.toInt())
            }
        })

        minutesAutoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculatePrice(daysAutoCompleteTextView, hoursAutoCompleteTextView, minutesAutoCompleteTextView, vehicleRentingPrice.toInt())
            }
        })

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

                        Log.d("response:", response.toString())

                        if(response.isNotBlank()){
                            var jsonObj = JSONObject(response)
                            jsonObj.getString("status")

                            Log.d("Response: ", response.toString())
                            if(jsonObj.getString("status") == "Booked"){
                                runOnUiThread{
                                    bookingConfirmed(jsonObj.getString("id"))
                                }
                                timer.cancel()
                            }
                            else if(jsonObj.getString("status") == "Cancelled"){
                                runOnUiThread{
                                    bookingCancelled()
                                }
                                timer.cancel()

                            }
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
                map["price"] = totalPrice.toString()

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

    private fun bookingConfirmed(id: String) {

        val i = Intent(this, CurrentlyActiveBooking::class.java)

        val bundle = Bundle()
        bundle.putString("days", rentingDays)
        bundle.putString("hours", rentingHours)
        bundle.putString("minutes", rentingMinutes)


        bundle.putString("renter", renterUsername)
        bundle.putString("rentee", username)
        bundle.putString("numberplate", renterVhNumberplate)
        bundle.putString("price", totalPrice.toString())
        bundle.putString("id", id)

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

    fun calculatePrice(
        daysAutoCompleteTextView: AutoCompleteTextView, hoursAutoCompleteTextView: AutoCompleteTextView,
        minutesAutoCompleteTextView: AutoCompleteTextView, pricePerDay: Int, ) {

        val days = daysAutoCompleteTextView.text.toString().toIntOrNull() ?: 0
        val hours = hoursAutoCompleteTextView.text.toString().toIntOrNull() ?: 0
        val minutes = minutesAutoCompleteTextView.text.toString().toIntOrNull() ?: 0

        val totalMinutes = (days * 24 * 60) + (hours * 60) + minutes
        totalPrice = totalMinutes * pricePerDay / (24 * 60)

        priceTextView.text = "$totalPrice PKR"
    }
}