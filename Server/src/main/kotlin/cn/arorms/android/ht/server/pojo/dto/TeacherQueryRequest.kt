package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.enums.Subject

data class TeacherQueryRequest (
    val keyword: String? = null,
    val subject: Subject? = null,
)
