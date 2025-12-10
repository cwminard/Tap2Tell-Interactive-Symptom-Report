package com.example.tap2tell.providerinfo

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tap2tell.R
import com.example.tap2tell.database.AppDb
import com.example.tap2tell.database.user
import kotlinx.coroutines.*

class providerdashboard : AppCompatActivity() {

    private lateinit var usersContainer: LinearLayout
    private lateinit var userRowTemplate: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.providerdashboard)

        usersContainer = findViewById(R.id.usersContainer)
        userRowTemplate = findViewById(R.id.userRowTemplate)

        loadUsers()
    }

    private fun loadUsers() {
        val dao = AppDb.getDatabase(this).userDao()

        CoroutineScope(Dispatchers.IO).launch {
            val allUsers = dao.getAllUsers()  // <-- REAL users

            withContext(Dispatchers.Main) {
                usersContainer.removeAllViews()

                if (allUsers.isEmpty()) {
                    val emptyText = TextView(this@providerdashboard).apply {
                        text = "No registered users yet."
                        textSize = 18f
                    }
                    usersContainer.addView(emptyText)
                    return@withContext
                }

                allUsers.forEach { u ->
                    addUserRow(u)
                }
            }
        }
    }

    private fun addUserRow(u: user) {

        // 🔥 Clone the template properly WITHOUT parent
        val row = layoutInflater.inflate(R.layout.user_clone_layout, null)

        val emailText = row.findViewById<TextView>(R.id.emailText)
        emailText.text = u.email

        row.setOnClickListener {
            val i = Intent(this, userdetails::class.java)
            i.putExtra("uid", u.firebaseuid)
            startActivity(i)
        }

        usersContainer.addView(row)
    }
}
