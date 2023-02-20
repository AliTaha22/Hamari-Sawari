package com.example.hamarisawari

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Chating : AppCompatActivity() {


    private val messages = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>
    val timer = Timer()
    lateinit var messagesListView: ListView
    lateinit var msg: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chating)


        messagesListView = findViewById(R.id.messagesListView)
        var sendbtn:Button=findViewById(R.id.sendButton)
        msg=findViewById(R.id.messageEditText)

        val bundle = intent.extras
        var sender = bundle?.getString("sender").toString()
        var receiver = bundle?.getString("receiver").toString()
        var table = bundle?.getString("table").toString()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messages)
        refreshMessage(sender, receiver, table)


        sendbtn.setOnClickListener {
            sendMessage(sender,receiver,msg.text.toString(),table)
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        timer.cancel()
    }

    private fun sendMessage(sender:String, receiver:String,message:String,table:String) {
        //val message = msg.text.toString().trim()
        //var table="$sender$receiver"
        val request: StringRequest = object : StringRequest(Method.POST, URLs().chating_URL,

            Response.Listener { response ->

                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                msg.text.clear()

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
//    private fun refreshMessage(sender:String, receiver:String,table:String) {
//        //var table="$sender-$receiver"
//
//        val request: StringRequest = object : StringRequest(
//            Request.Method.POST, URLs().chating_URL,
//            Response.Listener { response ->
//                Log.d("response.length: ", response.length.toString())
//                Log.d("RESPONSE: ", response.toString());
//                if(response.length>2) {
//                    //Log.d("Bike_DATA: ", response.length.toString());
//                    val json = JSONArray(response)
//                    Log.d("RESPONSE: ", json.toString());
//
//                    //Log.d("JSON1: ", json1.toString());
//                    var count = 0
//                    messages.clear()
//                    while (count < json.length()) {
//                        val json1 = JSONObject(json[count].toString())
//                        var va1=json1.getString("sender")
//                        var va2=json1.getString("msg")
//                        var m="$va1->   $va2"
//                        messages.add(m)
//                        count += 1
//                    }
//                }
//
//                 /*for (i in 0 until json.length()) {
//                     messages.add(json.getString(i))
//                 }*/
//
//            },
//            Response.ErrorListener { error ->
//                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
//            }){
//            override fun getParams(): Map<String, String> {
//                val map : MutableMap<String,String> = HashMap()
//                map["table"]=table
//                map["sender"]=sender
//                map["receiver"]=receiver
//                map["message"]="null"
//                map["check"]=1.toString()
//                return map
//            }
//        }
//        val queue = Volley.newRequestQueue(this)
//        queue.add(request)
//        handler.postDelayed({
//            // Recursively call the function
//            refreshMessage(sender, receiver, table)
//        }, 2000)
//    }

    private fun refreshMessage(sender:String, receiver:String,table:String){

        Log.d("Inside Function: ", "HELLO")
        var previousData = "Initialized"

        val task = object : TimerTask() {
            override fun run() {
                val request: StringRequest = object : StringRequest(
                    Method.POST, URLs().chating_URL,

                    Response.Listener { response ->

                        if(response.length>2) {

                            runOnUiThread{


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


                                messagesListView.adapter = adapter

                            }

                }

//                        Log.d("Response: ", response.toString())
//                        if(response.contains("Booked")){
//                            runOnUiThread{
//                                bookingConfirmed()
//                            }
//                            timer.cancel()
//                        }
//                        else if(response.contains("Cancelled")){
//                            runOnUiThread{
//                                bookingCancelled()
//                            }
//                            timer.cancel()
//
//                        }
                    },
                    Response.ErrorListener { error ->

                        Toast.makeText(this@Chating, error.toString(), Toast.LENGTH_LONG).show()

                        Log.d("My Error:", error.toString())
                    }) {

                    override fun getParams(): Map<String, String> {

                        val map: MutableMap<String, String> = HashMap()
                        map["table"]=table
                        map["sender"]=sender
                        map["receiver"]=receiver
                        map["message"]="null"
                        map["check"]=1.toString()
                        return map
                    }
                }
                val queue = Volley.newRequestQueue(this@Chating)
                queue.add(request)
            }
        }
        timer.schedule(task, 0, 2000)

    }



}