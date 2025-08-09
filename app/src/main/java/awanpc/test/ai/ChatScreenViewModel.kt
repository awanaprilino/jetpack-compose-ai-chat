package awanpc.test.ai

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import awanpc.test.ai.data.Message
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatScreenViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.apiKey
    )

    private val chat = generativeModel.startChat()
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())

    fun sendPrompt(
        bitmap: Bitmap?,
        prompt: String
    ) {
        // Add user message to the list
        val userMessage = Message(
            message = prompt,
            time = timeFormatter.format(Date()),
            isMe = true,
            image = bitmap
        )
        _messages.value += userMessage

        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = chat.sendMessage(
                    content {
                        bitmap?.let { image(it) }
                        text(prompt)
                    }
                )

                response.text?.let { outputContent ->
                    // Add AI response to the list
                    val aiMessage = Message(
                        message = outputContent,
                        time = timeFormatter.format(Date()),
                        isMe = false
                    )
                    _messages.value += aiMessage
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}