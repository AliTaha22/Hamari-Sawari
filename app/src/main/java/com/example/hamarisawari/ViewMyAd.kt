package com.example.hamarisawari

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.hamarisawari.com.example.hamarisawari.Adapters.VehicleImagePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ViewMyAd : AppCompatActivity() {

    lateinit var picture: ImageView
    lateinit var username: TextView
    lateinit var rating: RatingBar
    lateinit var ratingCount: TextView
    lateinit var vehicleName: TextView
    lateinit var vehicleManufacturer: TextView
    lateinit var vehiclePrice: TextView
    lateinit var vehicleSeatingCapacity: TextView
    lateinit var vehicleTransmission: TextView
    lateinit var vehicleModel: TextView
    lateinit var vehicleType: TextView
    lateinit var vehicleDescription: TextView

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_my_ad)

        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        var myUsername = mySharedPref.getString("username", null).toString()

        val bundle = intent.extras
        val numberplate = bundle?.getString("numberplate")
        val type = bundle?.getString("type")
        Log.d("username: ", myUsername.toString())
        Log.d("numberplate: ", numberplate.toString())
        Log.d("type: ", type.toString())


        picture = findViewById(R.id.renterPicture)
        username = findViewById(R.id.renterUsername)
        rating = findViewById(R.id.renterRating)
        ratingCount = findViewById(R.id.renterRatingCount)
        vehicleName = findViewById(R.id.vehName)
        vehicleManufacturer = findViewById(R.id.vehManufacturer)
        vehiclePrice = findViewById(R.id.vehPrice)
        vehicleSeatingCapacity = findViewById(R.id.seatingCapacity)
        vehicleTransmission = findViewById(R.id.vehTransmission)
        vehicleModel = findViewById(R.id.vehModel)
        vehicleType = findViewById(R.id.vehType)
        vehicleDescription = findViewById(R.id.vehDescription)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)


        fetchDetails(myUsername, numberplate, type)


    }

    private fun fetchDetails(username: String?, numberplate: String?, type: String?){

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().viewMoreDetails_URL,
            Response.Listener { response ->


                var array= JSONArray(response)

                Log.d("Response: ", array.toString())
                putDetailsOnScreen(array)


            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()

                map["username"] = username.toString()
                map["numberplate"] = numberplate.toString()
                map["type"] = type.toString()


                return map }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)



    }

    private fun putDetailsOnScreen(myArray: JSONArray) {


        var userJsonobj = JSONObject(myArray[0].toString())
        //var userJsonobj= JSONObject(userArrayData[0].toString())

        var vehicleArrayData = JSONArray(myArray[1].toString())
        var vehicleJsonobj= JSONObject(vehicleArrayData[0].toString())

        var i=1
        var images: ArrayList<String> = ArrayList()
        while(i<vehicleArrayData.length()){

            var job = JSONObject(vehicleArrayData[i].toString())
            Log.d("Vehicle IMG $i: ", job.toString())
            var a = job.getString("image")
            images?.add(URLs().images_URL + a)
            i+=1
        }
        viewPager.adapter = VehicleImagePagerAdapter(this, images)

// Connect the TabLayout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position -> }.attach()

        //Log.d("putDetailsOnScreen: ", userArrayData.toString())
        Log.d("putDetailsOnScreen2: ", vehicleArrayData.toString())

        username.text = userJsonobj.getString("username")
        try {
            ratingCount.text = userJsonobj.getString("rating_count")

        } catch (e: JSONException) {
            // Handle the JSON exception
            e.printStackTrace()

            // Set a default value for the 'ratingCount' text view
            ratingCount.text = "0"
        }
        rating.rating = userJsonobj.getString("rating").toFloat()
        vehicleName.text = vehicleJsonobj.getString("name")
        vehicleManufacturer.text = vehicleJsonobj.getString("manufacturer")
        vehiclePrice.text = vehicleJsonobj.getString("rentingprice")
        vehicleSeatingCapacity.text = vehicleJsonobj.getString("seatingcapacity")
        vehicleModel.text = vehicleJsonobj.getString("model")
        vehicleDescription.text = vehicleJsonobj.getString("description")
        if(vehicleSeatingCapacity.text as String > 2.toString())
        {
            vehicleTransmission.text = vehicleJsonobj.getString("transmission")
            vehicleType.text = vehicleJsonobj.getString("type")
        }
        else
        {
            vehicleTransmission.text = "Manual"
            vehicleType.text = "Bike"
        }
        Glide.with(this).load(URLs().images_URL + userJsonobj.getString("picture")).into(picture)

    }
}