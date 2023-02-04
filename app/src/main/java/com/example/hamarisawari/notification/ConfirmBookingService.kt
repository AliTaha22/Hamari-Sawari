package com.example.hamarisawari.com.example.hamarisawari.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.hamarisawari.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ConfirmBookingService: FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        var bundle = Bundle()
        Log.d("ONMSGRECVD22: ", message.data.toString())
        //message.data.toString()
        if (message.data.isNotEmpty()) {
            // Get the data payload
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
            bundle.putString("days", days)
            bundle.putString("hours", hours)
            bundle.putString("minutes", minutes)
            bundle.putString("VhType", VhType)
            bundle.putString("VhNumberplate", VhNumberplate)


        }
        showNotification(message.notification?.title.toString(), message.notification?.body.toString(),
            message.notification?.clickAction.toString(), bundle)



    }
    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, message: String, click_action: String, bundle: Bundle){

        Log.d("MYBNDL22: ", bundle.toString())
        // Create an explicit intent for an Activity in your app
        val intent = Intent(click_action).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtras(bundle)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, "102")
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
            notify(2, builder.build())
        }


    }



}