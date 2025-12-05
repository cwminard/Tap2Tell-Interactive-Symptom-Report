
package com.example.tap2tell

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Find the views by their IDs from the XML
        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupLink = findViewById<TextView>(R.id.signupLink)

        // --- Logic for the Login Button ---
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // For now, we'll do a simple check. Later, this will involve Firebase/a server.
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Display a success message
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                // Navigate to the main screen of the app
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                // Call finish() to prevent the user from coming back to the login screen by pressing the back button
                finish()

            } else {
                // Display an error message
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // --- Logic for the Signup Link (already done) ---
        signupLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
