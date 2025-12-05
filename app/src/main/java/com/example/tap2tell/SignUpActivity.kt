package com.example.tap2tell

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Find the views by their IDs
        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val signupButton = findViewById<Button>(R.id.signupButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        // --- Logic for the Signup Button ---
        signupButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Simple validation
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // In a real app, you would register the user with a backend service here.
                Toast.makeText(this, "Sign Up Successful! Please log in.", Toast.LENGTH_LONG).show()

                // Navigate back to the Login screen
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                // Call finish() to remove the signup screen from the back stack
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Logic for the Login Link (already done) ---
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
