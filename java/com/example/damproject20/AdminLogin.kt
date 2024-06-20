package com.example.damproject20


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class AdminLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val emailEditText: EditText = findViewById(R.id.etEmail)
        val passwordEditText: EditText = findViewById(R.id.etPassword)
        val signInButton: Button = findViewById(R.id.btnLogIn)

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and Password must not be empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            signIn(email, password)
        }
    }

    private fun admin() {
        val intent = Intent(this, AdminCurrentActivity::class.java)
        finish() // Ends the current activity and returns to the previous one
        startActivity(intent)
    }

    private fun signIn(email: String, password: String) {
        val url = "http://localhost/login.php"
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { response ->
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                val message = jsonResponse.getString("message")
                val admin = jsonResponse.getString("admin")
                println("Response: $message")
                if (success) {
                    if (admin == "1"){
                        // Handle successful login
                        Toast.makeText(this, "Admin Login successful", Toast.LENGTH_SHORT).show()
                        admin()
                    }
                    else
                    {
                        // Handle failed login
                        Toast.makeText(this, "You do not have admin permissions", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    // Handle failed login
                    Toast.makeText(this, "Login failed: $message", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                println("Error: ${error.message}")
                Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                params["password"] = password
                return params
            }
        }
        requestQueue.add(stringRequest)
    }
}