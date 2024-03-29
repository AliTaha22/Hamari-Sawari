package com.example.hamarisawari

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.Admin.AdminUserAdapter
import org.json.JSONArray

class AdminUserB : AppCompatActivity() {
    var dataList= ArrayList<users>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_b)


        var RV:RecyclerView=findViewById(R.id.AdminUserBRV)
        readpost(RV)


    }
    private fun readpost(rv: RecyclerView){
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().readUser_URL,
            Response.Listener { response ->

                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()


                Log.d("Bike_DATA: ", response.toString());
                var array= JSONArray(response)


                getUsersData(array) //0 index has car data
                //getVehiclesData(array.getJSONArray(1),1)  //1 index has bike data

                //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()

                //Log.d("Car_DATA: ", array.toString())


                //recycler view implementation
                rv.layoutManager= LinearLayoutManager(this)
                rv.adapter= this?.let { AdminUserAdapter(it, dataList,1) }
            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()
                map["check"]=0.toString()
                return map }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
    private fun getUsersData(array: JSONArray){

        var count=0
        while(count<array.length())
        {

            var jsonobj=array.getJSONObject(count)
            var userData = users(
                jsonobj?.getString("name"),
                jsonobj.getString("age"),
                jsonobj.getString("gender"),
                jsonobj.getString("contact"),
                jsonobj.getString("username"),
                jsonobj.getString("id"),
                jsonobj.getString("picture"),
                jsonobj.getString("rating"))
            //Log.d("Home FrG: ", veh.images?.get(0).toString())

            //Log.d("Home FrG: ", veh.images?.get(0).toString())
            if (userData != null) {
                dataList.add(userData) }
            count+=1

        }
    }
}