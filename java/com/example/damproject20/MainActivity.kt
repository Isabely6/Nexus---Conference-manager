package com.example.damproject20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the button by its ID
        val signupbutton: Button = findViewById(R.id.signUp_button)

        //Intenting new activity
        signupbutton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        // Find the button by its ID
        val loginbutton: Button = findViewById(R.id.logIn_button)

        //Intenting new activity
        loginbutton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

        // Find the button by its ID
        val adminbtn: ImageButton = findViewById(R.id.admin_button)

        //Intenting new activity
        adminbtn.setOnClickListener {
            val intent = Intent(this, AdminLogin::class.java)
            startActivity(intent)
        }


    }
}