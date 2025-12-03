package cn.arorms.android.ht.client.pojo.models

import java.time.LocalDateTime

data class Plan(
    val id: Long? = null,
    val userId: Long,
    val content: String,
    val deadline: String,  // 改为字符串类型以兼容服务器返回格式
    val isCompleted: Boolean
) {
    // 将字符串转换为 LocalDateTime 的辅助方法
    fun getDeadlineAsLocalDateTime(): LocalDateTime {
        return LocalDateTime.parse(deadline)
    }
}
