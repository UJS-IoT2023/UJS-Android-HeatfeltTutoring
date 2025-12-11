package cn.arorms.android.ht.client.ui.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.network.RetrofitClient
import cn.arorms.android.ht.client.pojo.dto.AgentRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.MediaType.Companion.toMediaType
import okio.IOException

data class ChatMessage(
    val id: String,
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class AIChatViewModel : ViewModel() {
    private val apiService = RetrofitClient.instance

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _currentStreamingMessage = MutableStateFlow<ChatMessage?>(null)
    val currentStreamingMessage: StateFlow<ChatMessage?> = _currentStreamingMessage.asStateFlow()

    fun sendMessage(message: String) {
        if (message.trim().isEmpty()) return

        val userId = AuthManager.getUserId()
        if (userId == 0L) {
            _error.value = "用户未登录"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            // Add user message
            val userMessage = ChatMessage(
                id = "user_${System.currentTimeMillis()}",
                content = message,
                isUser = true
            )
            _messages.value = _messages.value + userMessage

            // Add initial AI message placeholder
            val aiMessageId = "ai_${System.currentTimeMillis()}"
            val aiMessage = ChatMessage(
                id = aiMessageId,
                content = "",
                isUser = false
            )
            _messages.value = _messages.value + aiMessage
            _currentStreamingMessage.value = aiMessage

            try {
                // Create OkHttp request directly for streaming
                val json = """
                    {
                        "message": "${message.replace("\"", "\\\"")}",
                        "conversationId": $userId
                    }
                """.trimIndent()

                val requestBody = json.toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url("http://172.20.10.4:8080/api/ai/chat")
//                    .url("http://192.168.0.158:8080/api/ai/chat")
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .apply {
                        val token = AuthManager.getToken()
                        if (token.isNotEmpty()) {
                            addHeader("Authorization", "Bearer $token")
                        }
                    }
                    .build()

                val client = OkHttpClient.Builder()
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .build()

                client.newCall(request).enqueue(object : okhttp3.Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) {
                        _loading.value = false
                        _error.value = "发送消息失败: ${e.message}"
                        _currentStreamingMessage.value = null
                    }

                    override fun onResponse(call: okhttp3.Call, response: Response) {
                        _loading.value = false

                        if (!response.isSuccessful) {
                            _error.value = "请求失败: ${response.code}"
                            _currentStreamingMessage.value = null
                            return
                        }

                        val responseBody = response.body ?: return
                        handleStreamingResponse(responseBody, aiMessageId)
                    }
                })
            } catch (exception: Exception) {
                _loading.value = false
                _error.value = "发送消息失败: ${exception.message}"
                _currentStreamingMessage.value = null
            }
        }
    }

    private fun handleStreamingResponse(responseBody: okhttp3.ResponseBody, messageId: String) {
        try {
            responseBody.source().use { source ->
                while (!source.exhausted()) {
                    val line = source.readUtf8Line() ?: break

                    if (line.startsWith("data:")) {
                        val data = line.substring(5).trim()
                        if (data.isNotEmpty() && data != "[DONE]") {
                            // Update the current streaming message
                            val currentMessage = _currentStreamingMessage.value
                            if (currentMessage != null) {
                                val updatedMessage = currentMessage.copy(
                                    content = currentMessage.content + data
                                )
                                _currentStreamingMessage.value = updatedMessage

                                // Update in messages list
                                val currentMessages = _messages.value.toMutableList()
                                val index = currentMessages.indexOfFirst { it.id == messageId }
                                if (index != -1) {
                                    currentMessages[index] = updatedMessage
                                    _messages.value = currentMessages
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = "处理响应失败: ${e.message}"
        } finally {
            _currentStreamingMessage.value = null
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearMessages() {
        _messages.value = emptyList()
        _currentStreamingMessage.value = null
    }
}
