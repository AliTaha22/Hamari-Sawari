package com.example.hamarisawari


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit


class CurrentlyActiveBooking : AppCompatActivity() {

    private lateinit var days: String
    private lateinit var hours: String
    private lateinit var minutes: String

    lateinit var countdownTextView: TextView
    private var timer: CountDownTimer? = null
    private var endTime = 0L


    lateinit var renterUsername:String
    lateinit var renteeUsername:String
    lateinit var vehicleNumberPlate:String
    lateinit var totalPrice:String
    lateinit var _bookingID:String
    lateinit var renteeNum:String
    lateinit var renterNum:String




    lateinit var finishButton: Button
    lateinit var reportProblem: Button

    lateinit var myUsername:String
    lateinit var typE:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currently_active_booking)


        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        myUsername = mySharedPref.getString("username", null).toString()

        countdownTextView = findViewById(R.id.countDownText)
        finishButton = findViewById(R.id.finishBooking)
        reportProblem = findViewById(R.id.report)
        //making the buttons invisible.
        finishButton.visibility = View.GONE
        reportProblem.visibility = View.GONE

        renteeNum = ""
        renterNum = ""


        val bundle = intent.extras
        if (bundle != null) {
            days = bundle.getString("days", "0")
            hours = bundle.getString("hours", "0")
            minutes = bundle.getString("minutes", "0")

            renterUsername = bundle.getString("renter", "0")
            renteeUsername = bundle.getString("rentee", "0")
            vehicleNumberPlate = bundle.getString("numberplate", "0")
            totalPrice = bundle.getString("price", "0")
            _bookingID = bundle.getString("id", "0")

            fetchRenterNumber(renterUsername)
            fetchRenteeNumber(renteeUsername)

        }
        if(days != "0" || hours != "0" || minutes != "0"){
            // Convert the strings to millis
            val millis = (days.toInt() * 24 * 60 * 60 + hours.toInt() * 60 * 60 + minutes.toInt() * 60) * 1000
            endTime = System.currentTimeMillis() + millis
            startTimer(millis.toLong())

        }else{


            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            endTime = sharedPref.getLong("end_time", 0)
            // Calculate the remaining time
            val remainingTime = endTime - System.currentTimeMillis()
            if (remainingTime > 0) {
                startTimer(remainingTime)
            } else {
                countdownTextView.text = "Finished!"
                displayButtons()
            }
        }




        fetchBookingDetails()




        var msg: Button = findViewById(R.id.messageRenter)
        msg.setOnClickListener {

            if (myUsername == renteeUsername){

                val i = Intent(this, Chating::class.java)
                var table = "$renteeUsername$renterUsername"
                val bundle = Bundle()
                bundle.putString("sender", renteeUsername)
                bundle.putString("receiver", renterUsername)
                bundle.putString("table", table)

                i.putExtras(bundle)
                startActivity(i)

            } else{

                val i = Intent(this, Chating::class.java)
                var table = "$renteeUsername$renterUsername"
                val bundle = Bundle()
                bundle.putString("sender", renterUsername)
                bundle.putString("receiver", renteeUsername)
                bundle.putString("table", table)

                i.putExtras(bundle)
                startActivity(i)


            }


        }

        var call: Button = findViewById(R.id.callRenter)
        call.setOnClickListener {

            if (myUsername == renteeUsername){

                val dialIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$renterNum"))

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // If the app doesn't have the CALL_PHONE permission, request it
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
                } else {
                    // If the app already has the CALL_PHONE permission, start the phone call
                    startActivity(dialIntent)
                }

            } else{

                val dialIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$renteeNum"))

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // If the app doesn't have the CALL_PHONE permission, request it
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
                } else {
                    // If the app already has the CALL_PHONE permission, start the phone call
                    startActivity(dialIntent)
                }


            }


        }

        finishButton.setOnClickListener {

            MaterialAlertDialogBuilder(this)
                .setTitle("WARNING!")
                .setMessage(
                    "Are you sure you want to finish the deal? Make sure to press finish when the deal is completely done i.e. vehicle has been returned/ total amount has been paid. If there" +
                            "is any problem, report it immediately."
                )
                .setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }
                .setPositiveButton("Yes") { dialog, which ->
                    finishBooking()

                }
                .show()


        }
        reportProblem.setOnClickListener {

            startActivity(Intent(this, ProblemReport::class.java))
        }
    }
    private fun fetchRenterNumber(renterUsername: String) {

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().fetchMyNum_URL,
            Response.Listener { response ->

                renterNum = response.toString()

            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()

                map["username"] = renterUsername


                return map }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }

    private fun fetchRenteeNumber(renteeUsername: String) {

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().fetchMyNum_URL,
            Response.Listener { response ->

                renteeNum = response.toString()

            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()

                map["username"] = renteeUsername


                return map }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }
    private fun finishBooking() {

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().finishBooking_URL,
            Response.Listener { response ->

                Toast.makeText(this, response, Toast.LENGTH_LONG).show()

                var mySharedPref: SharedPreferences = getSharedPreferences("BookingInfo", MODE_PRIVATE)
                var dataEditor = mySharedPref.edit()
                dataEditor.putBoolean("Status", false)
                dataEditor.apply()
                dataEditor.commit()

                val i = Intent(this, PostBooking::class.java)

                val bundle = Bundle()

                if(myUsername == renterUsername) {
                    bundle.putString("rated_username", renteeUsername)
                }
                else {
                    bundle.putString("rated_username", renterUsername)
                }
                bundle.putString("table", "$renteeUsername$renterUsername")
                i.putExtras(bundle)
                startActivity(i)
                finish()

                //Log.d("My Response:", response.toString() )
            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()

                Log.d("My Error:", error.toString() )
            }){

            override fun getParams(): Map<String, String> {

                val map : MutableMap<String,String> = HashMap()
                map["myUsername"] = myUsername
                map["type"] = typE
                map["numberPlate"] = vehicleNumberPlate

                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun fetchBookingDetails() {


        var bookingID: TextView = findViewById(R.id.bookingID)
        var renterID: TextView = findViewById(R.id.renterID)
        var renteeID: TextView = findViewById(R.id.renteeID)
        var vhTypeID: TextView = findViewById(R.id.vehicleTypeID)
        var vhStatusID: TextView = findViewById(R.id.statusID)
        var vhPriceID: TextView = findViewById(R.id.amountID)


        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().bookingDetails_URL,
            Response.Listener { response ->

                val jsonObject = JSONObject(response)

                bookingID.text = jsonObject.getString("id")
                renterID.text = jsonObject.getString("renter")
                renteeID.text = jsonObject.getString("rentee")
                vhTypeID.text = jsonObject.getString("type")
                typE = jsonObject.getString("type")
                vhStatusID.text = jsonObject.getString("status")
                vhPriceID.text = totalPrice


                //Log.d("My Response:", response.toString() )
            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()

                Log.d("My Error:", error.toString() )
            }){

            override fun getParams(): Map<String, String> {

                val map : MutableMap<String,String> = HashMap()
                map["renterUsername"] = renterUsername
                map["renteeUsername"] = renteeUsername
                map["numberPlate"] = vehicleNumberPlate
                map["id"] = _bookingID
                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun startTimer(millis: Long) {

        timer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Calculate the remaining time
                val remainingTime = endTime - System.currentTimeMillis()
                if (remainingTime > 0) {
                    updateTimer(remainingTime)
                } else {
                    countdownTextView.text = "Finished!"
                    displayButtons()
                    cancel()
                }
            }

            override fun onFinish() {
                countdownTextView.text = "Finished!"
                displayButtons()
            }
        }.start()
    }

    private fun updateTimer(millisUntilFinished: Long) {
        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished).toInt()
        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

        countdownTextView.text = "$days Days $hours Hours $minutes Minutes $seconds Seconds"
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()

        // Store the end time in SharedPreferences
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong("end_time", endTime)
            apply()
        }
    }
    override fun onResume() {
        super.onResume()

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val savedEndTime = sharedPref.getLong("end_time", 0)
        val remainingTime = savedEndTime - System.currentTimeMillis()

        if (remainingTime > 0) {
            startTimer(remainingTime)
        } else {
            countdownTextView.text = "Finished!"
            displayButtons()
        }
    }
    private fun displayButtons() {
        if (endTime - System.currentTimeMillis() <= 0) {
            finishButton.visibility = View.VISIBLE
            reportProblem.visibility = View.VISIBLE
        }
    }

}