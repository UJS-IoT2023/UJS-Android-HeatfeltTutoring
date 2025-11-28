package cn.arorms.android.ht.server.dto

data class SendVerificationCodeRequest(
    val email: String
)

data class VerifyEmailRequest(
    val email: String,
    val code: String
)