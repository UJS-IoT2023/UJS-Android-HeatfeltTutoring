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
//    val verificationCode: String
//    val address: String? = null
)

data class SendVerificationCodeRequest(
    val email: String
)

data class VerifyEmailRequest(
    val email: String,
    val code: String
)
