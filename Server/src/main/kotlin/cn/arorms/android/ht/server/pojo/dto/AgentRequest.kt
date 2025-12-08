package cn.arorms.android.ht.server.pojo.dto

data class AgentRequest(
    val prompt: String? = null,
    val message: String,
    val conversationId: String  // Should be userId
)