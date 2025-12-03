package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.enums.Role

data class SelectUserRequest(
    val userId: Long? = null,
    val usernameKeyWord: String? = null,
    val role: Role? = null,
    val addressKeyWord: String? = null,
)