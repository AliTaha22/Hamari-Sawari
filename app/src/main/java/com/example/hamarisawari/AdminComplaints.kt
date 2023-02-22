package com.example.hamarisawari

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class AdminComplaints : AppCompatActivity() {

    var complaint=ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_complaints)


        var list:ListView=findViewById(R.id.complaintslist)



        readcomplaints(list)
        //Toast.makeText(this, complaint.toString(), Toast.LENGTH_SHORT).show()



    }




    private fun readcomplaints(list:ListView) {
        //var table="$sender-$receiver"
        val request: StringRequest = object : StringRequest(
            Request.Method.POST, URLs().complaint_URL,
            Response.Listener { response ->
//                Log.d("response.length: ", response.length.toString())
                Log.d("RESPONSE: ", response.toString());
                if(response.length>2) {
                    //Log.d("Bike_DATA: ", response.length.toString());
                    val json = JSONArray(response)
                    //Log.d("RESPONSE: ", json.toString());

                    //Log.d("JSON1: ", json1.toString());
                    var count = 0
                    complaint.clear()
                    while (count < json.length()) {
                        val json1 = JSONObject(json[count].toString())
                        var va1=json1.getString("username")
                        var va2=json1.getString("problem_description")
                        var m="Username: $va1\t complaint=> $va2"
                        complaint.add(m)
                        count += 1
                    }
                    adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, complaint)
                    list.adapter = adapter
                }

                /*for (i in 0 until json.length()) {
                    messages.add(json.getString(i))
                }*/

            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()
                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}