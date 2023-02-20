package com.example.hamarisawari

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ProblemReport : AppCompatActivity() {

    lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem_report)

        //fetching my username
        var mySharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        username = mySharedPref.getString("username", null).toString()

        var reportButton: Button = findViewById(R.id.reportProblemButton)
        var reportDescription: EditText = findViewById(R.id.reportEditText)

        reportButton.setOnClickListener {

            submitReport(reportDescription)

        }
    }

    private fun submitReport(reportDescription: EditText) {

        val request: StringRequest = object : StringRequest(
            Method.POST, URLs().submitReport_URL,
            Response.Listener { response ->


                reportDescription.text.clear()
                MaterialAlertDialogBuilder(this)
                    .setTitle("Report Response")
                    .setMessage("$response Press OK to continue." )
                    .setPositiveButton("OK") { dialog, which ->

                        finish()
                    }
                    .show()


            },
            Response.ErrorListener { error ->

                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()

            }){
            override fun getParams(): Map<String, String> {
                val map : MutableMap<String,String> = HashMap()

                map["myUsername"] = username
                map["reportDescription"] = reportDescription.text.toString()

                return map
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}