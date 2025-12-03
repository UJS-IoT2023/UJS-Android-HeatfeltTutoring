package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.enums.LoginType

data class LoginRequest(
    val loginType: LoginType,
    val identifier: String,
    val password: String
)

class LoginResponse(
    val token: String? = null,
    val userId: Long? = null,
    val username: String? = null,
    val message: String
) 