package cn.arorms.android.ht.server.dto

data class LoginRequest(
    val phoneNumber: String,
    val password: String
)

data class RegisterRequest(
    val phoneNumber: String,
    val password: String,
    val icon: String? = null,
    val address: String? = null
)

data class AuthResponse(
    val token: String,
    val userId: Long,
    val phoneNumber: String,
    val icon: String? = null,
    val address: String? = null,
    val message: String
)
