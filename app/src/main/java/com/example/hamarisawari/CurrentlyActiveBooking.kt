package com.example.hamarisawari

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.util.*
import java.util.concurrent.TimeUnit


class CurrentlyActiveBooking : AppCompatActivity() {

    private lateinit var days: String
    private lateinit var hours: String
    private lateinit var minutes: String
    lateinit var countdownTextView: TextView
    private var timer: CountDownTimer? = null
    private var remainingTime = 0L
    private var startTime = 0L
    lateinit var renterusername:String


    lateinit var finishButton: Button
    lateinit var reportProblem: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currently_active_booking)


        countdownTextView = findViewById(R.id.countDownText)
        finishButton = findViewById(R.id.finishBooking)
        reportProblem = findViewById(R.id.report)


        val bundle = intent.extras
        if (bundle != null) {
            days = bundle.getString("days", "0")
            hours = bundle.getString("hours", "0")
            minutes = bundle.getString("minutes", "0")
            renterusername = bundle.getString("renter", "0")

            // Convert the strings to millis
            val millis = (days.toInt() * 24 * 60 * 60 + hours.toInt() * 60 * 60 + minutes.toInt() * 60) * 1000
            startTime = System.currentTimeMillis()
            startTimer(millis.toLong())
        } else {
            val sharedPref = getPreferences(Context.MODE_PRIVATE)
            remainingTime = sharedPref.getLong("remaining_time", 0)
            startTime = sharedPref.getLong("start_time", 0)
            if (remainingTime > 0) {
                val timePassed = System.currentTimeMillis() - startTime
                startTimer(remainingTime - timePassed)
            } else {
                displayButtons()
            }
        }

        //making the buttons invisible.
        finishButton.visibility = View.GONE
        reportProblem.visibility = View.GONE

        finishButton.setOnClickListener {


            startActivity(Intent(this, PostBooking::class.java))

        }

        reportProblem.setOnClickListener {

            startActivity(Intent(this, ProblemReport::class.java))
        }
    }

    private fun startTimer(millis: Long) {
        timer = object : CountDownTimer(millis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                updateTimer(millisUntilFinished)
            }

            override fun onFinish() {
                countdownTextView.text = "Finished!"
                displayButtons()
            }
        }.start()
    }

    private fun updateTimer(millisUntilFinished: Long) {
        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished).toInt()
        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60

        countdownTextView.text = "$days Days $hours Hours $minutes Minutes $seconds Seconds"
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong("remaining_time", remainingTime)
            putLong("start_time", startTime)
            apply()
        }
    }
    private fun displayButtons() {
        finishButton.visibility = View.VISIBLE
        reportProblem.visibility = View.VISIBLE
    }

}