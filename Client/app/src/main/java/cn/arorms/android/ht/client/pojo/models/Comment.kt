package cn.arorms.android.ht.client.pojo.models

import java.time.LocalDateTime

data class Comment(
    val id: Long? = null,
    val fromUserId: Long,
    val fromUserName: String? = null,
    val toUserId: Long,
    val toUserName: String? = null,
    val content: String,
    val createdAt: LocalDateTime? = null,
)
