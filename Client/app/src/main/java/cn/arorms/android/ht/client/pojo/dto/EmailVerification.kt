package cn.arorms.android.ht.client.pojo.dto

data class EmailVerificationRequest(
    val email: String
)

data class EmailVerification(
    val email: String,
    val verrificationCode: String
)
