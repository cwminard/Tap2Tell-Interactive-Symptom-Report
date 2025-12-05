package com.example.tap2tell

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the logout button from the layout
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Set a click listener on the button
        logoutButton.setOnClickListener {
            // Display a toast message
            Toast.makeText(this, "You have been logged out.", Toast.LENGTH_SHORT).show()

            // Create an Intent to go back to the LoginActivity
            val intent = Intent(this, LoginActivity::class.java)

            // These flags are important for a good user experience
            // to clear the back stack and prevent the user from returning to the main screen.
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Start the LoginActivity
            startActivity(intent)

            // Call finish() on MainActivity to remove it from the back stack
            finish()
        }
    }
}
