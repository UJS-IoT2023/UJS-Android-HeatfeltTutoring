package cn.arorms.android.ht.server.dto
class RegisterResponse(
    val token: String? = null,
    val userId: Long? = null,
    val username: String,
    val message: String
)

class LoginResponse(
    val token: String? = null,
    val userId: Long? = null,
    val username: String? = null,
    val message: String
) 