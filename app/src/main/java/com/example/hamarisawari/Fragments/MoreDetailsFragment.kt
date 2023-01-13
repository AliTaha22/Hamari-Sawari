package com.example.hamarisawari.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentMoreDetailsBinding.inflate(inflater, container, false)

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


        //receiving data from bundle
        var mBundle: Bundle
        mBundle = arguments!!
         // key must be same which was given in first fragment


        //Log.d("USERNAME: ", mBundle.getString("username").toString())
        //Log.d("NUMBERPLATE: ", mBundle.getString("numberplate").toString())
        //Log.d("NUMBERPLATE: ", mBundle.getString("type").toString())

        fetchDetails(mBundle.getString("username"), mBundle.getString("numberplate"), mBundle.getString("type")  )

        // Inflate the layout for this fragment
        return binding!!.root
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
    }


}