package cn.arorms.android.ht.server.pojo.dto

import java.time.LocalDateTime

data class UserSummary(
    var id: Long? = null,
    var username: String,
    var avatarUrl: String? = null,
    var realName: String? = null,
    var gender: String? = null,
    var address: String? = null,
)
