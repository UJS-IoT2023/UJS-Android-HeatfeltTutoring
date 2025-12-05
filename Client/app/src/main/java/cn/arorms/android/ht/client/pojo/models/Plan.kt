package cn.arorms.android.ht.client.pojo.models

import java.time.LocalDateTime

data class Plan(
    val id: Long? = null,
    val userId: Long,
    val content: String,
    val deadline: String,
    val isCompleted: Boolean
) {
    fun getDeadlineAsLocalDateTime(): LocalDateTime {
        return LocalDateTime.parse(deadline)
    }
}
