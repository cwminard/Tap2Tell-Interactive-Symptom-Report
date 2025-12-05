package com.example.tap2tell

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// Inherit from AppCompatActivity to make this a screen
class LoginActivity : AppCompatActivity() {

    // This function is called when the activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This line connects your Kotlin code to your XML layout file.
        // We will create this layout file in the next step.
        setContentView(R.layout.activity_login)
    }
}
