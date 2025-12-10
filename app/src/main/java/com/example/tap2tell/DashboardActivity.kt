package com.example.tap2tell

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_dashboard)

        val chat1 = findViewById<ImageView>(R.id.Chat1)
        val iconMeds = findViewById<ImageView>(R.id.iconMeds)
        val iconSymptoms = findViewById<ImageView>(R.id.iconSymptoms)
        val iconProvider = findViewById<ImageView>(R.id.iconProvider)
        val icon2FA = findViewById<ImageView>(R.id.icon2FA)

        chat1.setOnClickListener {
            startActivity(Intent(this, Aiactivity::class.java))
        }

        iconSymptoms.setOnClickListener {
            startActivity(Intent(this, Symptoms::class.java))
        }

        iconMeds.setOnClickListener {
            Toast.makeText(this, "Medications feature coming soon", Toast.LENGTH_SHORT).show()
        }

        icon2FA.setOnClickListener {
            Toast.makeText(this, "2FA feature coming soon", Toast.LENGTH_SHORT).show()
        }

        iconProvider.setOnClickListener {
            startActivity(Intent(this, chat::class.java))
        }

        findViewById<LinearLayout>(R.id.cardMeds).setOnClickListener {
            Toast.makeText(this, "Medications feature coming soon", Toast.LENGTH_SHORT).show()
        }

        findViewById<LinearLayout>(R.id.card2FA).setOnClickListener {
            Toast.makeText(this, "2FA feature coming soon", Toast.LENGTH_SHORT).show()
        }


    }
}
