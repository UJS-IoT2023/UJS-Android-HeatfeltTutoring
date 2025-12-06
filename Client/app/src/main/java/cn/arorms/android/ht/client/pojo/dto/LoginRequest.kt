package cn.arorms.android.ht.client.pojo.dto

enum class LoginType {
    USERNAME, EMAIL, GOOGLE
}

data class LoginRequest(
    val loginType: LoginType = LoginType.USERNAME,
    val identifier: String,
    val password: String
)
