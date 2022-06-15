package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)


        var profile: Button = findViewById(R.id.btnProfile)
        var searchVh: Button = findViewById(R.id.btnSearchVehicle)
        var RentVh: Button = findViewById(R.id.btnRentVehicle)
        var viewVhs: Button = findViewById(R.id.btnViewVh)


        profile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }
        searchVh.setOnClickListener {
            startActivity(Intent(this, SearchVehicle::class.java))
        }
        RentVh.setOnClickListener {  }
        viewVhs.setOnClickListener {  }
    }
}