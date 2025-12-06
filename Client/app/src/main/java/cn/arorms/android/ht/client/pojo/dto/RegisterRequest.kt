package cn.arorms.android.ht.client.pojo.dto

enum class RegisterType{
    EMAIL,
    GOOGLE
}

data class RegisterRequest(
    val registerType: RegisterType? = RegisterType.EMAIL,
    val username: String,
    val email: String,
    val password: String,
)