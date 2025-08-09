package awanpc.test.ai.data

import android.graphics.Bitmap

data class Message(
    val message: String,
    val time: String,
    val isMe: Boolean,
    val image: Bitmap? = null,
)