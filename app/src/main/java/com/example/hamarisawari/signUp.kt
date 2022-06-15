package com.example.hamarisawari

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

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
        //button for signing up
        var signup: Button = findViewById(R.id.btnSignUp)


        signup.setOnClickListener {

        }
    }
}