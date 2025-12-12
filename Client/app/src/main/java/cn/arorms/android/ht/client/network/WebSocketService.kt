package cn.arorms.android.ht.client.network

import android.os.Handler
import android.os.Looper
import android.util.Log
import cn.arorms.android.ht.client.pojo.models.ChatMessage
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompHeader

object WebSocketService {

    private val gson = RetrofitClient.gson
    private val listeners = mutableListOf<(ChatMessage) -> Unit>()
    private var stompClient: StompClient? = null
    private val handler = Handler(Looper.getMainLooper())
    private var subscribedDialogueIds = mutableSetOf<Long>()
    private var isStompConnected = false

    fun connect() {
        if (stompClient?.isConnected == true) {
            return
        }

        // WebSocket endpoint should match HTTP API base URL
        val stompUrl = ServerConfig.WEBSOCKET_URL

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, stompUrl).apply {
            // Add authentication header if needed
            val token = AuthManager.getToken()
            if (token.isNotEmpty()) {
                val headers = listOf(StompHeader("Authorization", "Bearer $token"))
                connect(headers)
            } else {
                connect()
            }
        }

        stompClient?.lifecycle()?.subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED -> {
                    Log.d("WebSocket", "STOMP connection opened")
                    // Delay subscription to allow STOMP handshake to complete
                    handler.postDelayed({
                        Log.d("WebSocket", "Attempting to subscribe after handshake delay")
                        subscribedDialogueIds.forEach { subscribeToDialogue(it) }
                    }, 200) // 200ms delay should be sufficient for CONNECTED frame
                }
                ua.naiksoftware.stomp.dto.LifecycleEvent.Type.ERROR -> {
                    Log.e("WebSocket", "STOMP connection error", lifecycleEvent.exception)
                }
                ua.naiksoftware.stomp.dto.LifecycleEvent.Type.CLOSED -> {
                    Log.d("WebSocket", "STOMP connection closed")
                    isStompConnected = false
                }
                ua.naiksoftware.stomp.dto.LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                    Log.e("WebSocket", "Server heartbeat failed")
                }
            }
        }
    }

    fun subscribeToDialogue(dialogueId: Long) {
        if (stompClient?.isConnected != true) {
            Log.w("WebSocket", "Cannot subscribe: STOMP client not connected")
            subscribedDialogueIds.add(dialogueId)
            return
        }

        val topic = "/topic/dialogue/$dialogueId"
        stompClient?.topic(topic)?.subscribe { stompMessage ->
            try {
                val message = gson.fromJson(stompMessage.payload, ChatMessage::class.java)
                Log.d("WebSocket", "Received message: $message")
                handler.post {
                    listeners.forEach { listener ->
                        listener(message)
                    }
                }
            } catch (e: Exception) {
                Log.e("WebSocket", "Error parsing message", e)
            }
        }
        subscribedDialogueIds.add(dialogueId)
        Log.d("WebSocket", "Subscribed to dialogue: $dialogueId")
    }

    fun unsubscribeFromDialogue(dialogueId: Long) {
        // Note: The library doesn't provide direct unsubscribe, but we can track and re-subscribe on reconnect
        subscribedDialogueIds.remove(dialogueId)
        Log.d("WebSocket", "Unsubscribed from dialogue: $dialogueId")
    }

    fun disconnect() {
        stompClient?.disconnect()
        stompClient = null
        subscribedDialogueIds.clear()
    }

    fun sendMessage(dialogueId: Long, message: ChatMessage) {
        if (stompClient?.isConnected != true) {
            Log.w("WebSocket", "Cannot send message: STOMP client not connected")
            return
        }

        val destination = "/app/chat/$dialogueId/send"
        val messageJson = gson.toJson(message)

        stompClient?.send(destination, messageJson)
            ?.subscribe(
                { Log.d("WebSocket", "Message sent successfully") },
                { error: Throwable -> Log.e("WebSocket", "Error sending message", error) }
            )
    }

    fun addMessageListener(listener: (ChatMessage) -> Unit) {
        listeners.add(listener)
    }

    fun removeMessageListener(listener: (ChatMessage) -> Unit) {
        listeners.remove(listener)
    }

    fun isConnected(): Boolean {
        return stompClient?.isConnected == true
    }
}
