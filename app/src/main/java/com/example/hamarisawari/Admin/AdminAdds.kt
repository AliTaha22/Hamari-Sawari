package com.example.hamarisawari.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.R
import com.example.hamarisawari.URLs
import com.example.hamarisawari.vehicles
import org.json.JSONArray
import org.json.JSONObject

class AdminAdds : AppCompatActivity() {

    var dataList= ArrayList<vehicles>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_adds)


        var RV:RecyclerView=findViewById(R.id.AdminAddRV)
        readpost(RV)

    }

    private fun readpost(rv: RecyclerView){
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().readData_URL,
            Response.Listener { response ->

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()


                var array= JSONArray(response)
                var array1= JSONArray(array[0].toString())
                var array2= JSONArray(array[1].toString())


                Log.d("Car_DATA: ", array[0].toString());


                getVehiclesData(array.getJSONArray(0),0) //0 index has car data
                getVehiclesData(array.getJSONArray(1),1)  //1 index has bike data

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()

                //Log.d("Car_DATA: ", dataList.toString())


                //recycler view implementation
                rv.layoutManager= LinearLayoutManager(this)
                rv.adapter= this?.let { AdminAddAdapter(it, dataList) }
            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()
                map["username"]="admin"
                return map }
        }
        val queue = Volley.newRequestQueue(this)
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
                    dataList.add(vehicleData)
                }
            }
            count+=1

        }
    }
}