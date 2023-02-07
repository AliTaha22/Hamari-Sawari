package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class Chating : AppCompatActivity() {
    private val messages = ArrayList<String>()
    val handler = Handler()
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chating)
        val bundle = intent.extras
        var sender = bundle?.getString("sender").toString()
        var receiver = bundle?.getString("receiver").toString()
        var table = bundle?.getString("table").toString()
        var messagesListView:ListView=findViewById(R.id.messagesListView)
        var sendbtn:Button=findViewById(R.id.sendButton)
        var msg:EditText=findViewById(R.id.messageEditText)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messages)
        messagesListView.adapter = adapter

        sendbtn.setOnClickListener {
            sendMessage(sender,receiver,msg.text.toString(),table)
        }

        refreshMessage(sender, receiver, table)

    }



    private fun sendMessage(sender:String, receiver:String,message:String,table:String) {
        //val message = msg.text.toString().trim()
        //var table="$sender$receiver"
        val request: StringRequest = object : StringRequest(
            Request.Method.POST, URLs().chating_URL,
            Response.Listener { response ->
                //Log.d("Bike_DATA: ", response.toString());

                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()
                map["table"]=table
                map["sender"]=sender
                map["receiver"]=receiver
                map["message"]=message
                map["check"]=0.toString()
                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }
    private fun refreshMessage(sender:String, receiver:String,table:String) {
        //var table="$sender-$receiver"
        val request: StringRequest = object : StringRequest(
            Request.Method.POST, URLs().chating_URL,
            Response.Listener { response ->
                Log.d("response.length: ", response.length.toString())
                Log.d("RESPONSE: ", response.toString());
                if(response.length>2) {
                    //Log.d("Bike_DATA: ", response.length.toString());
                    val json = JSONArray(response)
                    Log.d("RESPONSE: ", json.toString());

                    //Log.d("JSON1: ", json1.toString());
                    var count = 0
                    messages.clear()
                    while (count < json.length()) {
                        val json1 = JSONObject(json[count].toString())
                        var va1=json1.getString("sender")
                        var va2=json1.getString("msg")
                        var m="$va1->   $va2"
                        messages.add(m)
                        count += 1
                    }
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
                map["table"]=table
                map["sender"]=sender
                map["receiver"]=receiver
                map["message"]="null"
                map["check"]=1.toString()
                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
        handler.postDelayed({
            // Recursively call the function
            refreshMessage(sender, receiver, table)
        }, 2000)
    }





}