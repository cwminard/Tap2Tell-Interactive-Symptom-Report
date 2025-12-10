package com.example.tap2tell

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Symptoms : AppCompatActivity() {

    private val symptoms = ArrayList<String>()
    private lateinit var symptomAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptoms)

        val inp = findViewById<EditText>(R.id.symptomEditText)
        val btn = findViewById<Button>(R.id.logSymptomButton)
        val lst = findViewById<ListView>(R.id.lstSymptoms)

        symptomAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, symptoms)
        lst.adapter = symptomAdapter

        btn.setOnClickListener {
            val text = inp.text.toString().trim()

            if (text.isNotEmpty()) {
                symptoms.add(text)
                symptomAdapter.notifyDataSetChanged()
                inp.text.clear()

                saveSymptom(text)

            }
        }
    }
    private fun saveSymptom(symptom: String) {
        val uid = intent.getStringExtra("uid")
            ?: return

        val ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(uid)
            .child("symptoms")
            .push()

        val data = mapOf(
            "text" to symptom,
            "timestamp" to System.currentTimeMillis()
        )
        ref.setValue(data)

    }
}