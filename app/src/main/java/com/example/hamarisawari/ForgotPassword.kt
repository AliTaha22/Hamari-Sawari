package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class ForgotPassword : AppCompatActivity() {

    var dataList= ArrayList<users>()
    //    lateinit var usern:String
    lateinit var otp:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.SEND_SMS),
            101)
        var userId: EditText=findViewById(R.id.uId)
        var usercontact: EditText=findViewById(R.id.uContact)
        var gotp:Button=findViewById(R.id.Gotp)


        gotp.setOnClickListener {

            verifyUser(userId.text.toString(),usercontact.text.toString())


        }


    }

    private fun verifyUser(Uid:String, uc:String){
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().forgotpass_URL,
            Response.Listener { response ->
//                Log.d("Bike_DATA: ", response.toString());
//                Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
                if(response.length>20) {
                    var array = JSONArray(response)
                    var count = 0
                    while (count < array.length()) {

                        var jsonobj = array.getJSONObject(count)
                        var userData = users(
                            jsonobj?.getString("name"),
                            jsonobj.getString("age"),
                            jsonobj.getString("gender"),
                            jsonobj.getString("contact"),
                            jsonobj.getString("username"),
                            jsonobj.getString("id"),
                            jsonobj.getString("picture"),
                            jsonobj.getString("rating")
                        )
                        //Log.d("Home FrG: ", veh.images?.get(0).toString())

                        //Log.d("Home FrG: ", veh.images?.get(0).toString())
                        if (userData != null) {
                            dataList.add(userData)
                        }
                        count += 1
                    }
                    if (uc.toString() == dataList[0].contact.toString()) {
                        var obj: SmsManager = SmsManager.getDefault()
                        val currentTimestamp = System.currentTimeMillis() % 100000
                        //Toast.makeText(this,currentTimestamp.toString() , Toast.LENGTH_SHORT).show()
                        obj.sendTextMessage(
                            dataList[0].contact.toString(),
                            null,
                            currentTimestamp.toString(),
                            null,
                            null
                        )
                        //startActivity(Intent(this, MainActivity::class.java))
                        val intent = Intent(this, ForgotPassword2::class.java)
                        intent.putExtra("OTP", currentTimestamp.toString())
                        intent.putExtra("username", dataList[0].username.toString())
                        startActivity(intent)


                    } else {
                        Toast.makeText(this, "Wrong UserName Or Contact Number", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Wrong UserName Or Contact Number", Toast.LENGTH_SHORT).show()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()
                map["ID"]=Uid
                map["Pass"]="Null"
                map["chk"]=1.toString()
                return map }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}