package cn.arorms.android.ht.client.dto

data class AuthResponse(
    val token: String,
    val userId: Long,
    val username: String? = null,
    val message: String
)