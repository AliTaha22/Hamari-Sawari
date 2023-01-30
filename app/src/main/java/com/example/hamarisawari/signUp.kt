package com.example.hamarisawari
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
class signUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        var name: EditText = findViewById(R.id.Name)
        var age: EditText = findViewById(R.id.Age)
        var gender: EditText = findViewById(R.id.Gender)
        var contact: EditText = findViewById(R.id.Contact)
        var id: EditText = findViewById(R.id.Id)
        var pass: EditText = findViewById(R.id.Password)
        var cnic:EditText=findViewById(R.id.CNIC)
        //button for signing up
        var signup: Button = findViewById(R.id.btnSignUp)
        signup.setOnClickListener {
            var Name = name.text.toString()
            var Age = age.text.toString()
            var Gender = gender.text.toString()
            var Contact = contact.text.toString()
            var Id = id.text.toString()
            var Pass = pass.text.toString()
            var Cnic = cnic.text.toString()
            if(Age.isNotEmpty() && Age.toInt() > 11 && Age.toInt() < 98)
            {
                age.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                if(Gender!=null && Gender=="MALE"||Gender=="FEMALE")
                {
                    gender.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                    if(Contact!=null && Contact.length==11 && Contact[0] == '0' && Contact[1] == '3')
                    {
                        contact.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                        if (Cnic!=null && Cnic.length==13)
                        {
                            cnic.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                            signUpUser(Name, Age, Gender, Contact, Id, Pass,Cnic)
                        }
                        else
                        {
                            cnic.background.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
                            Toast.makeText(this, "CNIC wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        contact.background.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
                        Toast.makeText(this, "Contact Format wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                else
                {
                    gender.background.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
                    Toast.makeText(this, "Gender Format incorrect", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                age.background.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
                Toast.makeText(this, "Age Format Incorrect", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun signUpUser(Name: String, Age: String, Gender: String, Contact: String, Id: String, Pass: String, Cnic: String ){
        val request: StringRequest = object : StringRequest(
            Request.Method.POST, URLs().signUp_URL,
            Response.Listener { response ->
                Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()
                Log.d("Bike_DATA: ", response.toString());
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
                map["cnic"]=Cnic
                map["block"]=1.toString()
                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}