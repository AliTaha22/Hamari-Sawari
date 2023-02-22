package com.example.hamarisawari.Fragments

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SeekBar
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
import com.example.hamarisawari.databinding.FragmentBikeSearchBinding
import com.example.hamarisawari.vehicles
import org.json.JSONArray
import org.json.JSONObject


class SearchBikeFragment : Fragment(R.layout.fragment_bike_search) {



    private var binding : FragmentBikeSearchBinding? = null
    lateinit var bikeArray: JSONArray
    lateinit var latitude: String
    lateinit var longitude: String
    var dataList= ArrayList<vehicles>()
    var locationList= ArrayList<String>()

    override fun onResume() {
        super.onResume()

        displayDropDown()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBikeSearchBinding.inflate(inflater, container, false)
        var priceSB: SeekBar = binding!!.priceSeekBar
        var locationSB: SeekBar = binding!!.locationSeekBar
        var priceBar = binding!!.priceBar
        var locationBar = binding!!. locationBar
        var manu=binding!!.manufacturer
        var trans=binding!!.engine
        var type=binding!!.color
        var model=binding!!.BikeModel
        var btn=binding!!.btnSearchVh
        var rv= binding?.searchRV
        var cm=model.text
        var mySharedPref = context?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        if (mySharedPref != null) {
            latitude = mySharedPref.getString("latitude",null).toString()
            longitude = mySharedPref.getString("longitude",null).toString()
        }

        displayDropDown()   //calling function that displays the dropdown menu

        //seekbars
        priceSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                priceBar.text = progress.toString()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        locationSB.setOnSeekBarChangeListener(object :  SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                locationBar.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        btn.setOnClickListener {

            if (rv != null && cm.isNotEmpty()) {

                //Toast.makeText(context, priceBar.text.toString(), Toast.LENGTH_SHORT).show()
                searchpost(rv!!,manu.text.toString(),trans.text.toString(),type.text.toString(),model.text.toString(),priceBar.text.toString(),locationBar.text.toString())

            }
            else
            {

                if (rv != null) {
                    dataList.clear()
                    locationList.clear()
                    rv.layoutManager= LinearLayoutManager(context)
                    rv.adapter= context?.let { homeAddAdapter(it, dataList, locationList) }
                }
                Toast.makeText(context, "Enter Bike Model too", Toast.LENGTH_SHORT).show()
            }

        }

        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }


    private fun displayDropDown()
    {
        val manufacturer = resources.getStringArray(R.array.BikeManufacturer)
        val engine = resources.getStringArray(R.array.BikeEngine)
        val color = resources.getStringArray(R.array.BikeColor)


        val arrayAdapterManufacturer = ArrayAdapter(requireContext(),
            R.layout.dropdown_menu, manufacturer)
        val arrayAdapterEngine = ArrayAdapter(requireContext(), R.layout.dropdown_menu, engine)
        val arrayAdapterColor = ArrayAdapter(requireContext(), R.layout.dropdown_menu, color)



        binding!!.manufacturer.setAdapter(arrayAdapterManufacturer)
        binding!!.engine.setAdapter(arrayAdapterEngine)
        binding!!.color.setAdapter(arrayAdapterColor)
    }


    private fun searchpost(rv: RecyclerView, manu:String, trans:String, type:String, model:String, price:String, Radius:String)
    {
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().searchData_URL,
            Response.Listener { response ->
                //Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                print(response.toString())
                Log.d("Bike_DATA: ", response.toString());
                var array= JSONArray(response)
                bikeArray=JSONArray(array[0].toString())
                dataList.clear()
                getBikesData(bikeArray,Radius)
                rv.layoutManager= LinearLayoutManager(context)
                rv.adapter= context?.let { homeAddAdapter(it, dataList, locationList) }
            },
            Response.ErrorListener { error ->

                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()
                map["manufacturer"]=manu
                map["transmission"]=trans
                map["type"]=type
                map["carModel"]=model
                map["price"]=price
                map["check"]= 1.toString()
                return map }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }
    private fun getBikesData(array: JSONArray,radius:String){

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
                if(radius[0].toString()>=distanceInKM.toString()) {
                    locationList.add(distanceString)
                    dataList.add(vehicleData)
                }
//                locationList.add(distanceString)
//                dataList.add(vehicleData)
            }
            count+=1

        }
    }
}