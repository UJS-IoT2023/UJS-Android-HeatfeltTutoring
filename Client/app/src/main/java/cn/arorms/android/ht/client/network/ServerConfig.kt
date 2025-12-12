package cn.arorms.android.ht.client.network

object ServerConfig {
    const val ADDRESS = "172.20.10.2"
    const val PORT = "8080"

    const val BASE_URL = "http://${ADDRESS}:${PORT}"

    const val WEBSOCKET_URL = "ws://${ADDRESS}:${PORT}/ws"

    const val AI_CHAT_URL = "$BASE_URL/api/ai/chat"
}
