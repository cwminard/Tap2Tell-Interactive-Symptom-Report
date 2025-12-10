package com.example.tap2tell

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tap2tell.database.AppDb
import com.example.tap2tell.providerinfo.providerdashboard
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.AlertDialog
import android.widget.EditText
import com.example.tap2tell.providerinfo.Providersignin
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private val ADMIN_CODE = "tap2tellAdmin2025"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val providerSignupLink = findViewById<TextView>(R.id.providersignup)

        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupLink = findViewById<TextView>(R.id.signupLink)
        val radioUser = findViewById<RadioButton>(R.id.radioUser)
        val radioProvider = findViewById<RadioButton>(R.id.radioProvider)
        val adminAccess = findViewById<TextView>(R.id.adminAccess)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val selectedRole = if (radioProvider.isChecked) "provider" else "user"

           if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener}

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    if (!result.user!!.isEmailVerified) {
                        result.user!!.sendEmailVerification()
                        Toast.makeText(
                            this,
                            "Verification email sent again. Please check your inbox.",
                            Toast.LENGTH_LONG
                        ).show()
                        auth.signOut()
                        return@addOnSuccessListener
                    }
                    val uid = result.user!!.uid  // Firebase UID

                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = AppDb.getDatabase(this@LoginActivity).userDao()
                        val user = dao.findByEmail(email)

                        withContext(Dispatchers.Main) {
                            if (user == null){
                                Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                            return@withContext
                            }

                            if (user.role == "pending_provider") {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Your provider account is awaiting approval.",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@withContext
                            }
                            if (user.role != selectedRole) {
                                Toast.makeText(this@LoginActivity, "Select correct role.", Toast.LENGTH_SHORT).show()
                                return@withContext
                            }

                            // Navigate
                            if (selectedRole == "provider") {
                                startActivity(Intent(this@LoginActivity, providerdashboard::class.java))
                            } else {
                                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                            }
                            finish()
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_LONG).show()
                }

        }
        providerSignupLink.setOnClickListener {
            startActivity(Intent(this, Providersignin::class.java))
        }

        signupLink.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // ADMIN ACCESS
        adminAccess.setOnClickListener {
            val input = EditText(this)
            input.hint = "Enter Admin Code"

            AlertDialog.Builder(this)
                .setTitle("Admin Login")
                .setMessage("Enter admin password to manage providers")
                .setView(input)
                .setPositiveButton("Enter") { _, _ ->
                    if (input.text.toString() == ADMIN_CODE) {
                        startActivity(Intent(this, AdminApprovalActivity::class.java))
                    } else {
                        Toast.makeText(this, "Incorrect admin password", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}