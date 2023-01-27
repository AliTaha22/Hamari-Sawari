package com.example.hamarisawari

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ContactAndCommunicationsRenter : AppCompatActivity() {

    lateinit var googleMap: GoogleMap
    lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_and_communications_renter)



        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        var myLatitude = mySharedPref.getString("latitude", null).toString()
        var myLongitude = mySharedPref.getString("longitude", null).toString()
        username = mySharedPref.getString("username", null).toString()



        //initializing map and setting a mark for the users & vehicle's location.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            val myLocation = LatLng(myLatitude.toDouble(), myLongitude.toDouble())
            googleMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10.0f))

            //val renterLocation = LatLng(renterLatitude.toDouble(), renterLongitude.toDouble())
            //googleMap.addMarker(MarkerOptions().position(renterLocation).title("Renter Location"))
        })


    }



}