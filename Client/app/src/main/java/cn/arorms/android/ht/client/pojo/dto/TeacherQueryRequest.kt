package cn.arorms.android.ht.client.pojo.dto

import cn.arorms.android.ht.client.pojo.enums.Subject

data class TeacherQueryRequest (
    val keyword: String? = null,
    val subject: Subject? = null,
)
