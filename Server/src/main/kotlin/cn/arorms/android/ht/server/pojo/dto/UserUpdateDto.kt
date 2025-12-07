package cn.arorms.android.ht.server.pojo.dto

data class UserUpdateDto(
    val username: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val realName: String? = null,
    val gender: String? = null,
    val wechatId: String? = null,
    val qqId: String? = null,
    val address: String? = null,
    val password: String? = null
)
