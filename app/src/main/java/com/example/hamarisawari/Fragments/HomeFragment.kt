package com.example.hamarisawari.Fragments

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.databinding.FragmentHomeBinding
import com.example.hamarisawari.homeAddAdapter
import com.example.hamarisawari.vehicles
import org.json.JSONArray
import org.json.JSONObject


class HomeFragment : Fragment(R.layout.fragment_home ) {

    lateinit var binding: FragmentHomeBinding
    var dataList= ArrayList<vehicles>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        binding =  FragmentHomeBinding.inflate(inflater, container, false)

        var rv=binding.homeRV
        readpost(rv)


        return binding!!.root
    }

    private fun readpost(rv: RecyclerView){
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().readData_URL,
            Response.Listener { response ->

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()


                var array= JSONArray(response)
                var array1=JSONArray(array[0].toString())
                var array2=JSONArray(array[1].toString())


                Log.d("Car_DATA: ", array[0].toString());


                getVehiclesData(array.getJSONArray(0),0) //0 index has car data
                getVehiclesData(array.getJSONArray(1),1)  //1 index has bike data

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()

                //Log.d("Car_DATA: ", dataList.toString())


                //recycler view implimentation
                rv.layoutManager= LinearLayoutManager(context)
                rv.adapter= context?.let { homeAddAdapter(it, dataList) }
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

    private fun getVehiclesData(array: JSONArray,flag:Int){

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


            if(flag==0) {
                var vehicleData = jsonobj?.getString("username")?.let {
                    vehicles(
                        it,
                        jsonobj.getString("rentingprice"),
                        jsonobj.getString("color"),
                        jsonobj.getString("manufacturer"),
                        jsonobj.getString("conditionn"),
                        jsonobj.getString("transmission"),
                        jsonobj.getString("type"),
                        jsonobj.getString("enginecapacity"),
                        jsonobj.getString("mileage"),
                        jsonobj.getString("model"),
                        jsonobj.getString("enginenumber"),
                        jsonobj.getString("numberplate"),
                        jsonobj.getString("description"),
                        images
                    )
                }

                //Log.d("Home FrG: ", veh.images?.get(0).toString())
                if (vehicleData != null) {
                    dataList.add(vehicleData)
                }
            }
            else{
                var vehicleData = jsonobj?.getString("username")?.let {
                    vehicles(
                        it,
                        jsonobj.getString("rentingprice"),
                        jsonobj.getString("color"),
                        jsonobj.getString("manufacturer"),
                        jsonobj.getString("conditionn"),
                        "Manual",
                        "Bike",
                        jsonobj.getString("enginecapacity"),
                        jsonobj.getString("mileage"),
                        jsonobj.getString("model"),
                        jsonobj.getString("enginenumber"),
                        jsonobj.getString("numberplate"),
                        jsonobj.getString("description"),
                        images
                    )
                }

                //Log.d("Home FrG: ", veh.images?.get(0).toString())
                if (vehicleData != null) {
                    dataList.add(vehicleData)
                }
            }
            count+=1

        }
    }

}