package com.example.tap2tell.providerinfo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tap2tell.R
import com.example.tap2tell.database.AppDb
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class userdetails : AppCompatActivity() {

    private lateinit var symptomsContainer: LinearLayout
    private lateinit var medContainer: LinearLayout
    private lateinit var userNameText: TextView
    private lateinit var chatButton: Button
    private var uid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_details)

        uid = intent.getStringExtra("uid") ?: ""

        symptomsContainer = findViewById(R.id.symptomsContainer)
        medContainer = findViewById(R.id.medContainer)
        userNameText = findViewById(R.id.username)
        chatButton = findViewById(R.id.chatButton)

        loadUserInfo()
        loadSymptoms()
        loadMedications()

        chatButton.setOnClickListener {
            val i = Intent(this, providerchat::class.java)
            i.putExtra("userUid", uid)
            startActivity(i)
        }
    }

    private fun loadUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDb.getDatabase(this@userdetails).userDao()
            val u = dao.findByUid(uid)

            withContext(Dispatchers.Main) {
                userNameText.text = u?.name ?: "Unknown User"
            }
        }
    }

    private fun loadSymptoms() {
        val symptomsRef = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .child("symptoms")

        symptomsContainer.removeAllViews()

        symptomsRef.get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                val empty = TextView(this)
                empty.text = "No symptoms reported."
                symptomsContainer.addView(empty)
                return@addOnSuccessListener
            }

            snapshot.children.forEach { item ->
                val symptomText = item.child("text").getValue(String::class.java) ?: "(Unknown symptom)"
                val view = TextView(this)
                view.text = "• $symptomText"
                symptomsContainer.addView(view)
            }
        }.addOnFailureListener {
            val error = TextView(this)
            error.text = "Failed to load symptoms."
            symptomsContainer.addView(error)
        }
    }

    private fun loadMedications() {
        val placeholder = TextView(this)
        placeholder.text = "No medications added."
        medContainer.addView(placeholder)
    }
}
