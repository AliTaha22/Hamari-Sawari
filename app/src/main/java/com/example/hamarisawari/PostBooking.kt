package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class PostBooking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_booking)

        var finishAll: Button = findViewById(R.id.finishAll)
        var skipRating: Button = findViewById(R.id.skipRating)


        finishAll.setOnClickListener {

            //write the code for updating the rating here.

            startActivity(Intent(this, MainMenu::class.java))
            finish()
        }

        skipRating.setOnClickListener {

            startActivity(Intent(this, MainMenu::class.java))
            finish()
        }

    }
}