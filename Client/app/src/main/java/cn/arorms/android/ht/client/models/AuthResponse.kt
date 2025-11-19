package cn.arorms.android.ht.client.models

data class AuthResponse(
    val token: String,
    val userId: Long,
    val phoneNumber: String,
    val username: String? = null,
    val icon: String? = null,
    val address: String? = null,
    val message: String
)
