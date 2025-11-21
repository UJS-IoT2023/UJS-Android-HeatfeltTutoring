package cn.arorms.android.ht.server.dto

class AuthResponse(
    val token: String? = null,
    val userId: Long? = null,
    val username: String? = null,
    val phoneNumber: String? = null,
    val icon: String? = null,
    val address: String? = null,
    val message: String
) 