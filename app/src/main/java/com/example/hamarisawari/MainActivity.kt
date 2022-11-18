package com.example.hamarisawari

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class MainActivity : AppCompatActivity() {



    private val url = "http://192.168.178.1/hamarisawari/login.php"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //creating a shared preference to store status of user i.e. logged in or not
        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        var loginStatus = mySharedPref.getBoolean("loggedIn", false)


        //accessing buttons from layout file
        var signup: Button = findViewById(R.id.signUp)
        var signIn: Button = findViewById(R.id.signIn)
        var forgotPass: Button = findViewById(R.id.forgotPassword)

        var signIn_ID: EditText = findViewById(R.id.id)
        var signIn_password: EditText = findViewById(R.id.Pass)


        //if the user is already logged in, this activity will be destroyed and user will be redirected to MainMenu.
        if(loginStatus)
        {
            startActivity(Intent(this@MainActivity, MainMenu::class.java))
            finish()
        }

        signup.setOnClickListener {

            startActivity(Intent(this, signUp::class.java))
        }

        signIn.setOnClickListener {

            var userId: String = signIn_ID.text.toString()
            var pass: String = signIn_password.text.toString()

            loginUser(userId, pass)

        }
        forgotPass.setOnClickListener {

            var q = Volley.newRequestQueue(applicationContext)

            //
        }





    }



    private fun loginUser(username: String,  password: String, ){

        val request: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->

                if(response.equals("Login Success")){

                    var mySharedPref = getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
                    var dataEditor = mySharedPref.edit()

                    //setting user status as true. Since the user is successfully logged in.
                    dataEditor.putBoolean("loggedIn", true)
                    dataEditor.putString("username", username)
                    dataEditor.apply()
                    dataEditor.commit()

                    //shifting to next activity and destroying the current activity i.e. MainActivity
                    val i = Intent(this, MainMenu::class.java)
                    startActivity(i)
                    finish()

                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show()

                }
                else
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show()

            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()

            }){

            override fun getParams(): Map<String, String> {


                val map : MutableMap<String,String> = HashMap()

                map["username"] = username
                map["password"] = password
                return map
            }
        }

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

}
