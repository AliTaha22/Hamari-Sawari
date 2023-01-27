package com.example.hamarisawari

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class CurrentlyActiveBooking : AppCompatActivity() {

    private val START_TIME_IN_MILLIS: Long = 150000
    private var mTextViewCountDown: TextView? = null
    private var mCountDownTimer: CountDownTimer? = null
    private var mTimerRunning = false
    private var mTimeLeftInMillis: Long = 0
    private var mEndTime: Long = 0
    private var remainingTimeInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currently_active_booking)

        mTextViewCountDown = findViewById(R.id.countDownText)
    }

    private fun startTimer() {
        mCountDownTimer = object : CountDownTimer(remainingTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeInMillis = millisUntilFinished
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                updateCountDownText()
                startTimer()
            }
        }.start()

        //mTimerRunning = true;
    }

    private fun updateCountDownText() {
        val minutes = (remainingTimeInMillis / 1000).toInt() / 60
        val seconds = (remainingTimeInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String =
            java.lang.String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        mTextViewCountDown!!.text = timeLeftFormatted
    }


    override fun onStop() {
        super.onStop()
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong("millisLeft", mTimeLeftInMillis)
        editor.putBoolean("timerRunning", mTimerRunning)
        editor.putLong("endTime", System.currentTimeMillis())
        editor.apply()
    }

    override fun onStart() {
        super.onStart()
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS)
        mTimerRunning = prefs.getBoolean("timerRunning", false)
        mEndTime = prefs.getLong("endTime", 0)
        if (mEndTime == 0L) {
            remainingTimeInMillis = mTimeLeftInMillis
        } else {
            var timeDiff = mEndTime - System.currentTimeMillis()
            //to convert into positive number
            timeDiff = Math.abs(timeDiff)
            val timeDiffInSeconds = timeDiff / 1000 % 60
            val timeDiffInMillis = timeDiffInSeconds * 1000
            remainingTimeInMillis = mTimeLeftInMillis - timeDiffInMillis
            var timeDiffInMillisPlusTimerRemaining = remainingTimeInMillis
            if (timeDiffInMillisPlusTimerRemaining < 0) {
                timeDiffInMillisPlusTimerRemaining = Math.abs(timeDiffInMillisPlusTimerRemaining)
                remainingTimeInMillis = START_TIME_IN_MILLIS - timeDiffInMillisPlusTimerRemaining
            }
        }
        updateCountDownText()
        startTimer()
    }


}
