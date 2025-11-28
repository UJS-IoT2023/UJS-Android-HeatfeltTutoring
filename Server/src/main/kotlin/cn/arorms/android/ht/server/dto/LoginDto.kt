package cn.arorms.android.ht.server.dto

data class LoginRequest(
    // TODO: Login with username
//    val username: String,
    val email: String,
    val password: String
)

class LoginResponse(
    val token: String? = null,
    val userId: Long? = null,
    val username: String? = null,
    val message: String
) 