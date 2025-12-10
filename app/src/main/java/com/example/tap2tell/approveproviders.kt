package com.example.tap2tell

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tap2tell.database.AppDb
import com.example.tap2tell.database.user
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminApprovalActivity : AppCompatActivity() {

    private lateinit var pendingContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.papproval)

        pendingContainer = findViewById(R.id.pendingContainer)
        loadPendingProviders()
    }

    private fun loadPendingProviders() {
        val dao = AppDb.getDatabase(this).userDao()

        CoroutineScope(Dispatchers.IO).launch {
            val pending = dao.getPendingProviders()

            withContext(Dispatchers.Main) {
                pendingContainer.removeAllViews()

                if (pending.isEmpty()) {
                    val text = TextView(this@AdminApprovalActivity).apply {
                        text = "No pending provider requests."
                        textSize = 16f
                    }
                    pendingContainer.addView(text)
                    return@withContext
                }

                pending.forEach { u ->
                    addPendingRow(u)
                }
            }
        }
    }

    private fun addPendingRow(u: user) {
        val row = layoutInflater.inflate(R.layout.item_pending, pendingContainer, false)

        val emailText = row.findViewById<TextView>(R.id.emailText)
        val approveButton = row.findViewById<Button>(R.id.approveButton)
        val rejectButton = row.findViewById<Button>(R.id.rejectButton)

        emailText.text = "${u.name.ifEmpty { "Unnamed" }} (${u.email})"

        // Approve provider
        approveButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                AppDb.getDatabase(this@AdminApprovalActivity)
                    .userDao()
                    .updateRole(u.firebaseuid, "provider")

                withContext(Dispatchers.Main) {
                    loadPendingProviders()
                }
            }
        }

        // Reject provider (delete account)
        rejectButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                AppDb.getDatabase(this@AdminApprovalActivity)
                    .userDao()
                    .deleteUser(u)

                withContext(Dispatchers.Main) {
                    loadPendingProviders()
                }
            }
        }

        pendingContainer.addView(row)
    }
}
