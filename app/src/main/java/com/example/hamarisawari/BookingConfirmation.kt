package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BookingConfirmation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_confirmation)

//        Log.d("Days",bundle?.getString("Days").toString())
//        Log.d("Hours",bundle?.getString("Hours").toString())
//        Log.d("Minutes",bundle?.getString("Minutes").toString())

        //fetching my username
        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        var username = mySharedPref.getString("username", null).toString()



        val extras = intent.extras
        var renteeUsername = extras?.getString("renteeUsername").toString()
        var rentingDays = extras?.getString("Days").toString()
        var rentingHours = extras?.getString("Hours").toString()
        var rentingMinutes = extras?.getString("Minutes").toString()
        var VhType = extras?.getString("VhType").toString()
        var VhNumberplate = extras?.getString("VhNumberplate").toString()
        var VhPrice = extras?.getString("VhPrice").toString()
        var bookingID = extras?.getString("bookingID").toString()

        //checkBookingRequest(username)


        MaterialAlertDialogBuilder(this)
            .setTitle("BOOKING CONFIRMATION")
            .setMessage("User $renteeUsername wants to book your vehicle for $rentingDays Days $rentingHours Hours AND $rentingMinutes Minutes." +
                    "For Amount: $VhPrice Do you accept this booking offer?")
            .setNegativeButton("No"){
                    dialog, which ->

                cancelBooking(username, renteeUsername, VhType, VhNumberplate,bookingID )
                dialog.cancel()
                startActivity(Intent(this, MainMenu::class.java))
                finish()

            }
            .setPositiveButton("Yes"){
                    dialog, which ->

                confirmBooking(username, renteeUsername, VhType, VhNumberplate,VhPrice,bookingID )
                val i = Intent(this, CurrentlyActiveBooking::class.java)

                val bundle = Bundle()
                bundle.putString("days", rentingDays)
                bundle.putString("hours", rentingHours)
                bundle.putString("minutes", rentingMinutes)

                bundle.putString("renter", username)
                bundle.putString("rentee", renteeUsername)
                bundle.putString("numberplate", VhNumberplate)
                bundle.putString("price", VhPrice)
                bundle.putString("id", bookingID)

                i.putExtras(bundle)
                startActivity(i)
                finish()
            }
            .show()




    }

//    private fun checkBookingRequest(username: String) :JSONObject{
//
//        var myJSON = JSONObject()
//        val request: StringRequest = object : StringRequest(
//            Method.POST, URLs().checkBookingRequest_URL,
//            Response.Listener { response ->
//
//                myJSON= JSONObject(response)
//
//                var Rentee = myJSON.getString("rentee")
//                var Days = myJSON.getString("Days")
//                var Hours = myJSON.getString("Hours")
//                var Minutes = myJSON.getString("Minutes")
//
//
//            },
//            Response.ErrorListener { error ->
//
//                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
//            }){
//
//            override fun getParams(): Map<String, String> {
//
//                val map : MutableMap<String,String> = HashMap()
//                map["renter"] = username
//                return map
//            }
//        }
//        val queue = Volley.newRequestQueue(this)
//        queue.add(request)
//
//        return myJSON
//    }

    private fun cancelBooking(myUsername: String, renteeUsername: String, VhType: String, VhNumberplate: String, bookingID: String) {

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
                map["type"] = VhType
                map["numberPlate"] = VhNumberplate
                map["bookingID"] = bookingID


                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


    private fun confirmBooking(myUsername: String, renteeUsername: String, VhType: String, VhNumberplate: String, VhPrice: String
    , bookingID: String) {

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().confirmBooking_URL,
            Response.Listener { response ->

                Toast.makeText(this, response, Toast.LENGTH_LONG).show()

                //Log.d("My Response:", response.toString() )
            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()

                Log.d("My Error:", error.toString())
            }) {

            override fun getParams(): Map<String, String> {

                val map: MutableMap<String, String> = HashMap()
                map["renterUsername"] = myUsername
                map["renteeUsername"] = renteeUsername
                map["type"] = VhType
                map["numberPlate"] = VhNumberplate
                map["rentingPrice"] = VhPrice
                map["bookingID"] = bookingID

                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}