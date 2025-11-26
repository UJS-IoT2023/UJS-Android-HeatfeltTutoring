package cn.arorms.android.ht.server.dto

data class LoginRequest(
    val username: String,
    val email: String,
//    val phoneNumber: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
//    val address: String? = null
)
