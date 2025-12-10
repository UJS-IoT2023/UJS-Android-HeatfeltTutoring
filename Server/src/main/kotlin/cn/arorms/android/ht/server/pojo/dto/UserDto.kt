package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.entity.User

data class UserDto(
    var id: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val avatarUrl: String? = null,
    val realName: String? = null,
    val gender: String? = null,
    val wechatId: String? = null,
    val qqId: String? = null,
    val address: String? = null,
    val password: String? = null,
    val teacherProfile: TeacherProfileDto? = null
) {
    constructor(user: User) : this(
        id = user.id,
        username = user.username,
        email = user.email,
        phoneNumber = user.phoneNumber,
        avatarUrl = user.avatarUrl,
        realName = user.realName,
        gender = user.gender,
        wechatId = user.wechatId,
        qqId = user.qqId,
        address = user.address,
        password = user.password,
        teacherProfile = user.teacherProfile?.let { TeacherProfileDto(it) }
    )
}
