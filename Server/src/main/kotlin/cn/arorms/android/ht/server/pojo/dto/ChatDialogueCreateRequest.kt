package cn.arorms.android.ht.server.pojo.dto

data class ChatDialogueCreateRequest(
    val participantIds: List<Long>,
    val title: String? = null
)
