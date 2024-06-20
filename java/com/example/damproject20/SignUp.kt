package com.example.damproject20

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val nameEditText: EditText = findViewById(R.id.etName)
        val userEmail: EditText = findViewById(R.id.etEmail)
        val passwordEditText: EditText = findViewById(R.id.etPassword)
        val signUpButton: Button = findViewById(R.id.btnRegister)
        val returnToLoginButton: TextView = findViewById(R.id.tvLogin)

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = userEmail.text.toString()
            val password = passwordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Call a function to send data to your local server
                //createUser(name, email, password)
                nameEditText.setText("")
                userEmail.setText("")
                passwordEditText.setText("")
                registerUser(this, name, email, password)
            } else {
                Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        returnToLoginButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            finish() // Ends the current activity and returns to the previous one
            startActivity(intent)
        }

    }

    private fun logIn(){
        val intent = Intent(this, LogIn::class.java)
        finish() // Ends the current activity and returns to the previous one
        startActivity(intent)
    }

    private fun registerUser(context: Context, name: String, email: String, password: String) {
        val url = "http://localhost/signup.php"

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                val message = jsonResponse.getString("message")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                if (success) {
                    // Handle successful login
                    Toast.makeText(this, "Sign Up successful", Toast.LENGTH_SHORT).show()
                    logIn()
                } else {
                    // Handle failed login
                    Toast.makeText(this, "Sign Up failed: $message", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["name"] = name
                params["email"] = email
                params["password"] = password
                return params
            }
        }
        requestQueue.add(stringRequest)
    }
}