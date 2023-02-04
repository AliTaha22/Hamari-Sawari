package com.example.hamarisawari.Fragments

import android.content.Context.MODE_PRIVATE
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.Adapters.homeAddAdapter
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.databinding.FragmentHomeBinding
import com.example.hamarisawari.vehicles
import org.json.JSONArray
import org.json.JSONObject


class HomeFragment : Fragment(R.layout.fragment_home ) {

    lateinit var binding: FragmentHomeBinding
    lateinit var showAllVehicles: Button
    lateinit var showCars: Button
    lateinit var showBikes: Button
    lateinit var carArray: JSONArray
    lateinit var bikeArray: JSONArray
    lateinit var latitude: String
    lateinit var longitude: String
    lateinit var username: String
    var dataList= ArrayList<vehicles>()
    var locationList= ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding =  FragmentHomeBinding.inflate(inflater, container, false)

        showAllVehicles = binding!!.showAll
        showCars = binding!!.showCars
        showBikes = binding!!.showBikes

        var rv=binding.homeRV
        readpost(rv)

        var mySharedPref = context?.getSharedPreferences("userInfo", MODE_PRIVATE)

        //fetching user's current location to store it as Vehicle's location of rent.
        if (mySharedPref != null) {
            latitude = mySharedPref.getString("latitude",null).toString()
            longitude = mySharedPref.getString("longitude",null).toString()
            username = mySharedPref.getString("username",null).toString()
        }


        //getCarsData(carArray)
        showAllVehicles.setOnClickListener {

            dataList.clear()
            getCarsData(carArray)
            getBikesData(bikeArray)

            //recycler view implimentation
            rv.layoutManager= LinearLayoutManager(context)
            rv.adapter= context?.let { homeAddAdapter(it, dataList, locationList) }
        }
        showCars.setOnClickListener {


            dataList.clear()
            getCarsData(carArray)

            //recycler view implimentation
            rv.layoutManager= LinearLayoutManager(context)
            rv.adapter= context?.let { homeAddAdapter(it, dataList, locationList) }
        }
        showBikes.setOnClickListener {


            dataList.clear()
            getBikesData(bikeArray)

            //recycler view implimentation
            rv.layoutManager= LinearLayoutManager(context)
            rv.adapter= context?.let { homeAddAdapter(it, dataList, locationList) }
        }

        return binding!!.root
    }

    private fun readpost(rv: RecyclerView){
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().readData_URL,
            Response.Listener { response ->

                Log.d("Bike_DATA: ", response.toString());
                var array= JSONArray(response)
                 carArray=JSONArray(array[0].toString())
                 bikeArray=JSONArray(array[1].toString())

            },
            Response.ErrorListener { error ->

                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()
                map["username"]=username
                return map }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    private fun getCarsData(array: JSONArray){

        var count=0
        while(count<array.length())
        {
            var arrayData = JSONArray(array[count].toString())
            var jsonobj= JSONObject(arrayData[0].toString())

            var i=1
            var images: ArrayList<String>? = ArrayList()
            while(i<arrayData.length()){

                var job = JSONObject(arrayData[1].toString())
                var a = job.getString("image")
                images?.add(a)
                i+=1

            }
                var vehicleData = jsonobj?.getString("username")?.let {
                    vehicles(
                        it,

                        jsonobj.getString("rentingprice"),
                        jsonobj.getString("color"),
                        jsonobj.getString("manufacturer"),
                        jsonobj.getString("seatingcapacity"),
                        jsonobj.getString("transmission"),
                        jsonobj.getString("type"),
                        jsonobj.getString("enginecapacity"),
                        jsonobj.getString("mileage"),
                        jsonobj.getString("model"),
                        jsonobj.getString("enginenumber"),
                        jsonobj.getString("numberplate"),
                        jsonobj.getString("description"),
                        jsonobj.getString("name"),
                        images,
                        jsonobj.getString("latitude"),
                        jsonobj.getString("longitude")
                    )
                }

                //Log.d("Home FrG: ", veh.images?.get(0).toString())
                if (vehicleData != null) {
                    val loc1 = Location("")

                    loc1.latitude = latitude.toDouble()
                    loc1.longitude = longitude.toDouble()

                    val loc2 = Location("")
                    loc2.latitude = vehicleData.latitude.toDouble()
                    loc2.longitude = vehicleData.longitude.toDouble()

                    val distanceInMeters: Float = loc1.distanceTo(loc2)
                    val distanceInKM  = (distanceInMeters / 1000)
                    val distanceString = String.format("%.1f KM", distanceInKM)

                    locationList.add(distanceString)
                    dataList.add(vehicleData)
                }
            count+=1

        }
    }

    private fun getBikesData(array: JSONArray){

        var count=0
        while(count<array.length())
        {
            var arrayData = JSONArray(array[count].toString())
            var jsonobj= JSONObject(arrayData[0].toString())

            var i=1
            var images: ArrayList<String>? = ArrayList()
            while(i<arrayData.length()){

                var job = JSONObject(arrayData[1].toString())
                var a = job.getString("image")
                images?.add(a)
                i+=1

            }

                var vehicleData = jsonobj?.getString("username")?.let {
                    vehicles(
                        it,

                        jsonobj.getString("rentingprice"),
                        jsonobj.getString("color"),
                        jsonobj.getString("manufacturer"),
                        jsonobj.getString("seatingcapacity"),
                        "Manual",
                        "Bike",
                        jsonobj.getString("enginecapacity"),
                        jsonobj.getString("mileage"),
                        jsonobj.getString("model"),
                        jsonobj.getString("enginenumber"),
                        jsonobj.getString("numberplate"),
                        jsonobj.getString("description"),
                        jsonobj.getString("name"),
                        images,
                        jsonobj.getString("latitude"),
                        jsonobj.getString("longitude")
                    )
                }

                //Log.d("Home FrG: ", veh.images?.get(0).toString())
            if (vehicleData != null) {
                val loc1 = Location("")
                loc1.latitude = latitude.toDouble()
                loc1.longitude = longitude.toDouble()

                val loc2 = Location("")
                loc2.latitude = vehicleData.latitude.toDouble()
                loc2.longitude = vehicleData.longitude.toDouble()

                val distanceInMeters: Float = loc1.distanceTo(loc2)
                val distanceInKM  = (distanceInMeters / 1000)
                val distanceString = String.format("%.1f KM", distanceInKM)

                //val distanceInKM = distance.toString() + "KM"

                //Log.d("MY DISTANCE: ", distance.toString())
                locationList.add(distanceString)
                dataList.add(vehicleData)
            }
            count+=1

        }
    }



}