package com.example.tap2tell.providerinfo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.tap2tell.R
import com.example.tap2tell.chatmsg.ChatMsg
import com.google.firebase.database.*

class providerchat : AppCompatActivity() {

    private val messages = ArrayList<String>()
    private lateinit var messageAdapter: ArrayAdapter<String>

    private lateinit var db: DatabaseReference
    private lateinit var roomId: String
    private lateinit var userUid: String

    // 🔥 HARD-CODED provider UID (MUST MATCH chat.kt)
    private val providerUid = "provider12345"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val lst = findViewById<ListView>(R.id.lst)
        val btn = findViewById<Button>(R.id.btn)
        val inp = findViewById<EditText>(R.id.inp)

        // UI adapter
        messageAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messages)
        lst.adapter = messageAdapter

        // UID of the user we clicked on in userdetails
        userUid = intent.getStringExtra("userUid") ?: return


        roomId = "$userUid-$providerUid"

        // Firebase reference
        db = FirebaseDatabase.getInstance()
            .getReference("chats")
            .child(roomId)
            .child("messages")

        // SEND button
        btn.setOnClickListener {
            val text = inp.text.toString().trim()
            if (text.isEmpty()) return@setOnClickListener

            val msg = ChatMsg(
                senderUid = providerUid,
                text = text
            )

            db.push().setValue(msg)
            inp.text.clear()
        }

        db.get().addOnSuccessListener { snapshot ->
            snapshot.children.forEach { child ->
                val msg = child.getValue(ChatMsg::class.java) ?: return@forEach
                val prefix =
                    if (msg.senderUid == userUid) "You: " else "Provider: "
                messages.add(prefix + msg.text)
            }
            messageAdapter.notifyDataSetChanged()
        }

        db.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previous: String?) {
                val msg = snapshot.getValue(ChatMsg::class.java) ?: return

                val prefix =
                    if (msg.senderUid == providerUid) "You: "
                    else "User: "

                messages.add(prefix + msg.text)
                messageAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previous: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previous: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
