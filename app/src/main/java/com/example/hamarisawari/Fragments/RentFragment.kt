package com.example.hamarisawari.Fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
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
import com.example.hamarisawari.*
import com.example.hamarisawari.Adapters.myVehiclesAdapter
import com.example.hamarisawari.databinding.FragmentRentBinding
import org.json.JSONArray
import org.json.JSONObject


class RentFragment : Fragment(R.layout.fragment_rent) {



    //var encoded_image: ArrayList<String>? = ArrayList()
    private var binding : FragmentRentBinding?=null
    var dataList= ArrayList<MyVehiclesClass>()

    lateinit var username: String
    lateinit var btnRentCar: Button
    lateinit var btnRentBike: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        binding = FragmentRentBinding.inflate(inflater, container, false)


        //fetching username
        var mySharedPref = context?.getSharedPreferences("userInfo", MODE_PRIVATE)
        username = mySharedPref!!.getString("username", null).toString()



        //initializing buttons
        btnRentCar = binding!!.rentCar
        btnRentBike = binding!!.rentBike


        btnRentCar.setOnClickListener {

            startActivity(Intent(context, RentCar::class.java))
        }
        btnRentBike.setOnClickListener {

            startActivity(Intent(context, RentBike::class.java))
        }


        var rv= binding!!.myVehiclesRV
        readpost(rv)

        return binding!!.root
    }


    private fun readpost(rv: RecyclerView){
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().viewMyVehicles_URL,
            Response.Listener { response ->

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()


                var array= JSONArray(response)
                Log.d("My Response: ", response.toString());


//                Log.d("Car_DATA: ", array[0].toString());

                getVehiclesData(array.getJSONArray(0),0) //0 index has car data
                getVehiclesData(array.getJSONArray(1),1)  //1 index has bike data

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()

                //Log.d("Car_DATA: ", dataList.toString())


                //recycler view implementation
                rv.layoutManager= LinearLayoutManager(context)
                rv.adapter= context?.let { myVehiclesAdapter(it, dataList) }
            },
            Response.ErrorListener { error ->

                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()

                map["username"] = username

                return map
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    private fun getVehiclesData(array: JSONArray, flag:Int){

        var count=0
        while(count<array.length()) //no. of vehs in array
        {
            var arrayData = JSONArray(array[count].toString()) //count = index no, its an array with both pics and details
            var jsonobj= JSONObject(arrayData[0].toString()) // this is the current index accessing only details
            var job = JSONObject(arrayData[1].toString()) // this is the current index accessing only pics
            var image = job.getString("image")


            var vehicleData  = MyVehiclesClass(jsonobj.getString("name"),image,jsonobj.getString("numberplate"), jsonobj.getString("seatingcapacity").toInt() )
            dataList.add(vehicleData)
            count+=1

        }

    }


}