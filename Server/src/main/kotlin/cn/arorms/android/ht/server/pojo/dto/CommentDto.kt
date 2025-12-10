package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.entity.Comment
import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime

data class CommentDto @JsonCreator constructor(
    val id: Long? = null,
    val fromUserId: Long,
    val fromUserName: String? = null,
    val toUserId: Long,
    val toUserName: String? = null,
    val content: String,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
) {
    constructor(comment: Comment): this(
        id = comment.id,
        fromUserId = comment.fromUser.id!!,
        fromUserName = comment.fromUser.username,
        toUserId = comment.toUser.id!!,
        toUserName = comment.toUser.username,
        content = comment.content,
        createdAt = comment.createdAt!!
    )
}
