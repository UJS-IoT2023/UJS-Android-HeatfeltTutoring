package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.enums.Subject

data class SelectTeacherRequest (
    val teacherUserId: Long? = null,
    val name: String? = null,
    val subjects: Subject? = null,
    val address: String? = null,
)