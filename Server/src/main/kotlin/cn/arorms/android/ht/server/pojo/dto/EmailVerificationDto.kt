package cn.arorms.android.ht.server.pojo.dto

data class SendVerificationCodeRequest(
    val email: String
)

data class VerifyEmailRequest(
    val email: String,
    val code: String
)