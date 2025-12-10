package com.example.tap2tell

import android.R.attr.apiKey
import android.R.attr.text
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.ArrayList

class Aiactivity : AppCompatActivity(){
    private val messages = ArrayList<String>()
    private lateinit var messageAdapter: ArrayAdapter<String>

    private val client = OkHttpClient()
    private val mediaTypeJson = "application/json; charset=utf-8".toMediaType()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ai_chat)

        val lst = findViewById<ListView>(R.id.ailst)
        val btn = findViewById<Button>(R.id.aibtn)
        val inp = findViewById<EditText>(R.id.aiinp)

        messageAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messages)
        lst.adapter = messageAdapter

        btn.setOnClickListener {
            val text = inp.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            messages.add("You: $text")
            messageAdapter.notifyDataSetChanged()
            inp.text.clear()

            /*detectAndSaveSymptom(text)*/

            callGemini(text)
        }
    }

    private fun callGemini(prompt: String){
        //temporary position for api key
        val apikey = "apikey goes here"

        val url = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent?key=$apikey"





        val json = JSONObject()
        val content = org.json.JSONArray()
        val partsArray = org.json.JSONArray()
        val partObj = JSONObject()
        partObj.put("text", prompt)
        partsArray.put(partObj)
        val contentObj = JSONObject()
        contentObj.put("parts", partsArray)
        content.put(contentObj)
        json.put("contents", content)

        val requestBody = RequestBody.create(mediaTypeJson, json.toString())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        Thread{
            try {
                val response = client.newCall(request).execute()
                val bodyString = response.body?.string() ?: ""

                if (!response.isSuccessful || bodyString.isEmpty()) {
                    runOnUiThread {
                        messages.add("AI: Error → ${response.code} | ${bodyString}")
                        messageAdapter.notifyDataSetChanged()
                    }
                    return@Thread
                }

                val root = JSONObject(bodyString)
                val candidates = root.optJSONArray("candidates")
                val first = candidates?.optJSONObject(0)
                val content = first?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val firstPart = parts?.optJSONObject(0)
                val aiText = firstPart?.optString("text") ?: "(no reply)"

                runOnUiThread {
                    messages.add("Betsy: $aiText")
                    messageAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    messages.add("AI: (request failed)")
                    messageAdapter.notifyDataSetChanged()
                }
            }
        }.start()
    }
    // save triggerwords ai pick up and send to provider symptom tab(works but does not go to right location)
    /*private fun detectAndSaveSymptom(message: String) {

        // Simple symptom keyword scan
        val symptomKeywords = listOf(
            "pain", "ache", "hurts", "fever", "cough", "cold",
            "headache", "nausea", "vomit", "tired", "weak"
        )

        val lower = message.lowercase()
        if (!symptomKeywords.any { lower.contains(it) }) return

        val userUid = intent.getStringExtra("uid") ?: return
        val ref = FirebaseDatabase.getInstance()
            .getReference("users")
            .child(userUid)
            .child("symptoms")
            .push()

        val symptomData = mapOf(
            "text" to text,
            "timestamp" to System.currentTimeMillis()
        )

        ref.setValue(symptomData)
    }*/

}