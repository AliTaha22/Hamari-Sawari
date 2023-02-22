package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hamarisawari.Fragments.HomeFragment
import org.json.JSONArray

class ForgotPassword2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password2)



        var Uotp:EditText=findViewById(R.id.EOTP)
        var Upass:EditText=findViewById(R.id.npas)
        var Ubtn:Button=findViewById(R.id.upPas)

        var Ootp=intent.getStringExtra("OTP")
        var Uname=intent.getStringExtra("username")


        Ubtn.setOnClickListener {
            if(Ootp.toString()==Uotp.text.toString()) {
                Toast.makeText(this, Ootp.toString(), Toast.LENGTH_SHORT).show()
                updatePass(Uname.toString(),Upass.text.toString())
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
        }

    }


    private fun updatePass(Uid:String, up:String){
        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().forgotpass_URL,
            Response.Listener { response ->
//                Log.d("Bike_DATA: ", response.toString());
                Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()

            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()
                map["ID"]=Uid
                map["Pass"]=up
                map["chk"]=0.toString()
                return map }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)

    }
}