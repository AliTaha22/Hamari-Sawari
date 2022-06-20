package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //accessing buttons from layout file
        var signup: Button = findViewById(R.id.signUp)
        var signIn: Button = findViewById(R.id.signIn)

        var signIn_ID: EditText = findViewById(R.id.id)
        var signIn_password: EditText = findViewById(R.id.Pass)

        signup.setOnClickListener {

            startActivity(Intent(this, signUp::class.java))
        }

        signIn.setOnClickListener {

            var id: String = signIn_ID.text.toString()
            var pass: String = signIn_password.text.toString()
            startActivity(Intent(this, MainMenu::class.java))
        }



    }
}