package com.example.hamarisawari

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)


        var Add:Button=findViewById(R.id.AdminAdd)
        var User:Button=findViewById(R.id.AdminUser)





        User.setOnClickListener {
            startActivity(Intent(this,AdminUser::class.java))
        }
        Add.setOnClickListener {
            startActivity(Intent(this,AdminAdds::class.java))
        }
    }
}