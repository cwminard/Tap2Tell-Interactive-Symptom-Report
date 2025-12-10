package com.example.tap2tell

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tap2tell.database.AppDb

import com.example.tap2tell.database.user
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // This runs ONCE and only if the provider doesn't already exist
        val db = AppDb.getDatabase(this)
        val userDao = db.userDao()

        CoroutineScope(Dispatchers.IO).launch {
            val providerEmail = "provider1@tap2tell.com"
            val existing = userDao.findByEmail(providerEmail)

            if (existing == null) {
                userDao.insert(
                    user(
                        firebaseuid = "provider1",
                        name = "Provider One",
                        email = providerEmail,
                        role = "provider"
                    )
                )
            }
        }

        // --- LOGOUT BUTTON LOGIC ---
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        logoutButton.setOnClickListener {
            Toast.makeText(this, "You have been logged out.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
