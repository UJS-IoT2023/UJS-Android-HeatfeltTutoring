package cn.arorms.android.ht.server.pojo.dto

import java.time.LocalDateTime

data class PlanDto(
    var userId: Long,
    var content: String,
    var deadline: LocalDateTime,
    var isCompleted: Boolean
)
