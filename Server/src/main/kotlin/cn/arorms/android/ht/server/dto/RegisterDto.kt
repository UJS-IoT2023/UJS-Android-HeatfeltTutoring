package cn.arorms.android.ht.server.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
//    val verificationCode: String
//    val address: String? = null
)

class RegisterResponse(
    val token: String? = null,
    val userId: Long? = null,
    val username: String,
    val message: String
)
