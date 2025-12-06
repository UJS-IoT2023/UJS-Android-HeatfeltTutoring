package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.enums.RegisterType

data class RegisterRequest(
    val registerType: RegisterType ?= RegisterType.EMAIL,
    val username: String,
    val email: String,
    val password: String,
)

class RegisterResponse(
    val token: String? = null,
    val userId: Long? = null,
    val username: String,
    val message: String
)
