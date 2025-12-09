package cn.arorms.android.ht.server.pojo.dto

data class ChatDialogueCreateRequest(
    val creatorId: Long,
    val participantIds: List<Long>, // 其他参与者ID，不包括创建者
    val title: String? = null // 群聊标题，可选
)
