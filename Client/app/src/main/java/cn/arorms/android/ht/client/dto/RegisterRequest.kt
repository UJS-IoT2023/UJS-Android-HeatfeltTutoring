package cn.arorms.android.ht.client.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)