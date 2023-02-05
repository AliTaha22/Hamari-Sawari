package com.example.hamarisawari.com.example.hamarisawari.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hamarisawari.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseService : FirebaseMessagingService() {


    private val CHANNEL_ID = "101"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        var bundle = Bundle()
        Log.d("ONMSGRECVD: ", message.data.toString())
        //message.data.toString()

        if (message.data["messageType"].equals("notifyRenter")) {
            val data: Map<String, String> = message.data

            // Extract the data fields you need
            val renteeUsername = data["rentee"]
            val renterUsername = data["renter"]
            val renterVehicleType = data["type"]
            val renterVehicleNumberplate = data["numberplate"]
            val renteeLatitude = data["renteeLatitude"]
            val renteeLongitude = data["renteeLongitude"]

            //putting the data inside a bundle
            bundle.putString("rentee", renteeUsername)
            bundle.putString("renter", renterUsername)
            bundle.putString("VehicleType", renterVehicleType)
            bundle.putString("Numberplate", renterVehicleNumberplate)
            bundle.putString("renteeLatitude", renteeLatitude)
            bundle.putString("renteeLongitude", renteeLongitude)

        } else if (message.data["messageType"].equals("requestBooking")) {
            val data: Map<String, String> = message.data

            // Extract the data fields you need
            val renteeUsername = data["renteeUsername"]
            val days = data["days"]
            val hours = data["hours"]
            val minutes = data["minutes"]
            val VhType = data["VhType"]
            val VhNumberplate = data["VhNumberplate"]

            //putting the data inside a bundle
            bundle.putString("renteeUsername", renteeUsername)
            bundle.putString("Days", days)
            bundle.putString("Hours", hours)
            bundle.putString("Minutes", minutes)
            bundle.putString("VhType", VhType)
            bundle.putString("VhNumberplate", VhNumberplate)
        }
//        if (message.data.isNotEmpty()) {
//            // Get the data payload
//
//
//        }
        showNotification(message.notification?.title.toString(), message.notification?.body.toString(),
                        message.notification?.clickAction.toString(), bundle)

    }
    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, message: String, click_action: String, bundle: Bundle){
        Log.d("MYBNDL: ", bundle.toString())

        // Create an explicit intent for an Activity in your app
        val intent = Intent(click_action).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        //here the data which is received from notification payload is being sent to the extras of click_action activity.
        intent.putExtras(bundle)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(true) //you cannot dismiss the notification by setting this


        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }


    }

}