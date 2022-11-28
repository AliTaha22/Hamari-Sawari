package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class signUp : AppCompatActivity() {


    private val url = "http://192.168.100.157/hamarisawari/signup.php"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        var name: EditText = findViewById(R.id.Name)
        var age: EditText = findViewById(R.id.Age)
        var gender: EditText = findViewById(R.id.Gender)
        var contact: EditText = findViewById(R.id.Contact)
        var id: EditText = findViewById(R.id.Id)
        var pass: EditText = findViewById(R.id.Password)

        //button for signing up
        var signup: Button = findViewById(R.id.btnSignUp)


        signup.setOnClickListener {
            var Name = name.text.toString()
            var Age = age.text.toString()
            var Gender = gender.text.toString()
            var Contact = contact.text.toString()
            var Id = id.text.toString()
            var Pass = pass.text.toString()


            signUpUser(Name, Age, Gender, Contact, Id, Pass)

        }
    }


    private fun signUpUser(Name: String, Age: String, Gender: String, Contact: String, Id: String, Pass: String ){

        val request: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->

                Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()

            }){

            override fun getParams(): Map<String, String> {


                val map : MutableMap<String,String> = HashMap()

                map["fullname"] = Name
                map["age"] = Age
                map["gender"] = Gender
                map["contact"] = Contact
                map["username"] = Id
                map["password"] = Pass
                return map
            }
        }

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}