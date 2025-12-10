package com.example.tap2tell.chatmsg

data class ChatMsg(
    val senderUid: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
