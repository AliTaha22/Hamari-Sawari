package com.example.hamarisawari.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.hamarisawari.R

class AdminLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        var admins:Button=findViewById(R.id.AdminsignIn)
        var id:EditText=findViewById(R.id.adminid)
        var pass:EditText=findViewById(R.id.APass)


        admins.setOnClickListener {
            var AdminId=id.text.toString()
            var adminpass=pass.text.toString()
            if(AdminId=="admin"&&adminpass=="admin")
            {
                startActivity(Intent(this,AdminMainActivity::class.java))
                finish()
            }
            else
            {
                Toast.makeText(this,"Wrong Id & Password",Toast.LENGTH_LONG).show()
            }
        }





    }
}