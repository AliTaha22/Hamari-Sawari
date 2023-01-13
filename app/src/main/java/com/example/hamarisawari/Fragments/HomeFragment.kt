package com.example.hamarisawari.Fragments

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
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.Adapters.homeAddAdapter
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

    var dataList= ArrayList<vehicles>()

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



        showAllVehicles.setOnClickListener {

            dataList.clear()
            getCarsData(carArray)
            getBikesData(bikeArray)

            //recycler view implimentation
            rv.layoutManager= LinearLayoutManager(context)
            rv.adapter= context?.let { homeAddAdapter(it, dataList) }
        }
        showCars.setOnClickListener {


            dataList.clear()
            getCarsData(carArray)

            //recycler view implimentation
            rv.layoutManager= LinearLayoutManager(context)
            rv.adapter= context?.let { homeAddAdapter(it, dataList) }
        }
        showBikes.setOnClickListener {


            dataList.clear()
            getBikesData(bikeArray)

            //recycler view implimentation
            rv.layoutManager= LinearLayoutManager(context)
            rv.adapter= context?.let { homeAddAdapter(it, dataList) }
        }


        return binding!!.root
    }

    private fun readpost(rv: RecyclerView){
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().readData_URL,
            Response.Listener { response ->

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()


                var array= JSONArray(response)

                 carArray=JSONArray(array[0].toString())
                 bikeArray=JSONArray(array[1].toString())


                Log.d("Car_DATA: ", array[0].toString());


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
                        images
                    )
                }

                //Log.d("Home FrG: ", veh.images?.get(0).toString())
                if (vehicleData != null) {
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
                        images
                    )
                }

                //Log.d("Home FrG: ", veh.images?.get(0).toString())
                if (vehicleData != null) {
                    dataList.add(vehicleData)
                }
            count+=1

        }
    }



}