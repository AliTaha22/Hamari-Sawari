package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap

class PostBooking : AppCompatActivity() {


    lateinit var rated_username: String
    lateinit var table: String
    var ratingValue: Float = 0F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_booking)

        var finishAll: Button = findViewById(R.id.finishAll)
        var skipRating: Button = findViewById(R.id.skipRating)
        var ratingBar: RatingBar = findViewById(R.id.rating)



        val extras = intent.extras
        //val extras = intent.getBundleExtra("AA")
        //Log.d("Extras", extras.toString())
        if (extras != null) {
            rated_username = extras.getString("rated_username").toString()
            table = extras.getString("table").toString()
        }


        finishAll.setOnClickListener {
            ratingValue = ratingBar.rating
            Log.d("Rating: ", ratingValue.toString())
            //write the code for updating the rating here.
            rateUser()

        }

        skipRating.setOnClickListener {

            startActivity(Intent(this, MainMenu::class.java))
            finish()
        }

    }

    private fun rateUser() {


        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().rateUser_URL,
            Response.Listener { response ->

                Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainMenu::class.java))
                finish()

                //Log.d("My Response:", response.toString() )
            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()

                Log.d("My Error:", error.toString() )
            }){

            override fun getParams(): Map<String, String> {

                val map : MutableMap<String,String> = HashMap()
                map["rated_username"] = rated_username
                map["rating_value"] = ratingValue.toString()
                map["table"] = table

                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}