package com.example.tap2tell.providerinfo

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tap2tell.R
import com.example.tap2tell.database.AppDb
import com.example.tap2tell.database.user
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Providersignin : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.providersignup)

        auth = FirebaseAuth.getInstance()

        val pname = findViewById<TextInputEditText>(R.id.pname)
        val pemail = findViewById<TextInputEditText>(R.id.pemail)
        val ppassword = findViewById<TextInputEditText>(R.id.ppassword)
        val submit = findViewById<Button>(R.id.submit)

        submit.setOnClickListener {
            val name = pname.text.toString()
            val email = pemail.text.toString()
            val password = ppassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val uid = result.user!!.uid  // Firebase UID
                    result.user!!.sendEmailVerification()
                    CoroutineScope(Dispatchers.IO).launch {
                        AppDb.getDatabase(this@Providersignin)
                            .userDao()
                            .insert(
                                user(
                                    firebaseuid = uid,
                                    name = name,
                                    email = email,
                                    role = "pending_provider"
                                )
                            )
                    }

                    Toast.makeText(this, "Account created!, waiting approval", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}


