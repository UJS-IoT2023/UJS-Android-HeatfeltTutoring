package cn.arorms.android.ht.server.dto

data class LoginRequest(
    val phoneNumber: String,
    val password: String
)

data class RegisterRequest(
    val phoneNumber: String,
    val password: String,
    val address: String? = null
)
