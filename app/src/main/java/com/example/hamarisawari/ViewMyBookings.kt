package com.example.hamarisawari

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.com.example.hamarisawari.Adapters.bookingAdapter
import com.example.hamarisawari.com.example.hamarisawari.bookingDataClass
import org.json.JSONArray
import org.json.JSONObject


class ViewMyBookings : AppCompatActivity() {

    lateinit var myUsername: String
    lateinit var bookingsRV: RecyclerView
    var dataList= ArrayList<bookingDataClass>()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_my_bookings)

        //fetching users username.
        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        myUsername = mySharedPref.getString("username", null).toString()

        bookingsRV = findViewById(R.id.viewMyBookingsRV)
        checkMyBookings()

    }

    private fun checkMyBookings(){
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().checkMyBookings_URL,
            Response.Listener { response ->

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()


                var myJSONArray = JSONArray(response)
                var i = 0
                while(i<myJSONArray.length()){

                    var job = JSONObject(myJSONArray[i].toString())
                    var bookingDataObj = bookingDataClass(job.getString("id"),
                                                        job.getString("renter"),
                                                        job.getString("rentee"),
                                                        job.getString("numberplate"),
                                                        job.getString("type"),
                                                        job.getString("renteelat"),
                                                        job.getString("renteelong"),
                                                        job.getString("renterlat"),
                                                        job.getString("renterlong"),
                                                        job.getString("price"),
                                                        job.getString("rentingprice"),
                                                        job.getString("status"))
                    dataList.add(bookingDataObj)
                    i+=1

                }
                bookingsRV.layoutManager= LinearLayoutManager(this)
                bookingsRV.adapter= this?.let { bookingAdapter(it, dataList, myUsername) }

            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()

                map["myUsername"] = myUsername

                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}