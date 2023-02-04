package com.example.hamarisawari.Fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.hamarisawari.*
import com.example.hamarisawari.databinding.FragmentMoreDetailsBinding
import org.json.JSONArray
import org.json.JSONObject

class MoreDetailsFragment : Fragment(R.layout.fragment_more_details) {

    lateinit var binding: FragmentMoreDetailsBinding

    lateinit var picture: ImageView
    lateinit var username: TextView
    lateinit var rating: RatingBar
    lateinit var vehicleImage: ImageView
    lateinit var vehicleName: TextView
    lateinit var vehicleManufacturer: TextView
    lateinit var vehiclePrice: TextView
    lateinit var vehicleSeatingCapacity: TextView
    lateinit var vehicleTransmission: TextView
    lateinit var vehicleModel: TextView
    lateinit var vehicleType: TextView
    lateinit var vehicleDescription: TextView

    lateinit var contact: Button

    lateinit var latitude: String
    lateinit var longitude: String

    private val CHANNEL_ID = "101"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentMoreDetailsBinding.inflate(inflater, container, false)
        var mySharedPref = context?.getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)

        //getting ids of all the details of xml.
        bindUserDetails()



        //receiving data from bundle
        var mBundle: Bundle
        mBundle = arguments!!
         // key must be same which was given in first fragment
        var renterUsername = mBundle.getString("username")
        var numberPlate = mBundle.getString("numberplate")
        var typE = mBundle.getString("type")




        //Log.d("USERNAME: ", mBundle.getString("username").toString())
        //Log.d("NUMBERPLATE: ", mBundle.getString("numberplate").toString())
        //Log.d("NUMBERPLATE: ", mBundle.getString("type").toString())

        //using this function to fetch the details of the VEHICLE-AD owner
        fetchDetails(renterUsername, numberPlate, typE)


        contact.setOnClickListener {

            val i = Intent(activity, ContactAndCommunications::class.java)

            val bundle = Bundle()
            bundle.putString("latitude", latitude)
            bundle.putString("longitude", longitude)
            bundle.putString("username", renterUsername)
            bundle.putString("vhtype", typE)
            bundle.putString("vhnumberplate", numberPlate)


            //here when the user clicks on contact button, a notification is sent to the owner of vehicle.
            createNotificationChannel()
            //when the notification is sent, the status of his/her vehicle also changes from Available to Pending
            //until further proceedings. it is again set to available if owner rejects booking request.
            sendNotificationToRenter(mySharedPref!!.getString("username", null).toString(),
                                    renterUsername.toString(), numberPlate.toString(), typE.toString() )

            i.putExtras(bundle)
            startActivity(i)
        }


        // Inflate the layout for this fragment
        return binding!!.root
    }

    private fun bindUserDetails() {

        picture = binding!!.renterPicture
        username = binding!!.renterUsername
        rating = binding!!.renterRating
        vehicleImage = binding!!.vehicleImage
        vehicleName = binding!!.vehName
        vehicleManufacturer = binding!!.vehManufacturer
        vehiclePrice = binding!!.vehPrice
        vehicleSeatingCapacity = binding!!.seatingCapacity
        vehicleTransmission = binding!!.vehTransmission
        vehicleModel = binding!!.vehModel
        vehicleType = binding!!.vehType
        vehicleDescription = binding!!.vehDescription

        contact = binding!!.contactRenter
    }


    private fun fetchDetails(username: String?, numberplate: String?, type: String?){

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().viewMoreDetails_URL,
            Response.Listener { response ->

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()


                var array= JSONArray(response)

                //carArray= JSONArray(array[0].toString())
                //bikeArray= JSONArray(array[1].toString())

                putDetailsOnScreen(array)

                //Log.d("More Details: ", array.toString())
                //Log.d("More Details2: ", array[1].toString())


                //getVehiclesData(array.getJSONArray(0),0) //0 index has car data
                //getVehiclesData(array.getJSONArray(1),1)  //1 index has bike data

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()

                //Log.d("Car_DATA: ", dataList.toString())

            },
            Response.ErrorListener { error ->

                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()

                map["username"] = username.toString()
                map["numberplate"] = numberplate.toString()
                map["type"] = type.toString()


                return map }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)



    }

    private fun putDetailsOnScreen(myArray: JSONArray) {


        var userJsonobj = JSONObject(myArray[0].toString())
        //var userJsonobj= JSONObject(userArrayData[0].toString())

        var vehicleArrayData = JSONArray(myArray[1].toString())
        var vehicleJsonobj= JSONObject(vehicleArrayData[0].toString())
        var vehicleJsonobjImg= JSONObject(vehicleArrayData[1].toString())

        //Log.d("putDetailsOnScreen: ", userArrayData.toString())
        Log.d("putDetailsOnScreen2: ", vehicleArrayData.toString())

        username.text = userJsonobj.getString("username")
        rating.numStars = userJsonobj.getString("rating").toInt()
        //vehicleImage = URLs().images_URL + vehicleJsonobj.getString("picture")
        vehicleName.text = vehicleJsonobj.getString("name")
        vehicleManufacturer.text = vehicleJsonobj.getString("manufacturer")
        vehiclePrice.text = vehicleJsonobj.getString("rentingprice")
        vehicleSeatingCapacity.text = vehicleJsonobj.getString("seatingcapacity")
//        vehicleTransmission = vehicleJsonobj.getString("transmission")
        vehicleModel.text = vehicleJsonobj.getString("model")
        //vehicleType.text = vehicleJsonobj.getString("type")
        vehicleDescription.text = vehicleJsonobj.getString("description")


        context?.let { Glide.with(it).load(URLs().images_URL + userJsonobj.getString("picture")).into(picture) }
        context?.let { Glide.with(it).load(URLs().images_URL + vehicleJsonobjImg.getString("image")).into(vehicleImage) }

        latitude = vehicleJsonobj.getString("latitude")
        longitude = vehicleJsonobj.getString("longitude")
    }

    private fun sendNotificationToRenter(myUsername: String, renterUsername: String, numberPlate: String, typE: String) {


        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().notifyRenter_URL,
            Response.Listener { response ->

                Toast.makeText(context, response, Toast.LENGTH_LONG).show()

                //Log.d("My Response:", response.toString() )
            },
            Response.ErrorListener { error ->

                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()

                Log.d("My Error:", error.toString() )
            }){

            override fun getParams(): Map<String, String> {

                val map : MutableMap<String,String> = HashMap()
                map["renteeUsername"] = myUsername
                map["renterUsername"] = renterUsername
                map["type"] = typE
                map["numberPlate"] = numberPlate

                return map
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)

    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Vehicle Booking Request"
            val descriptionText = "Someone wants to book your vehicle"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}