package cn.arorms.android.ht.client.pojo.models

import java.time.LocalDateTime

data class TeacherSummary(

    var id: Long? = null,
    var username: String,
    var email: String,
    var phoneNumber: String? = null,
    var avatarUrl: String? = null,
    var realName: String? = null,
    var sex : String? = null,
    var address: String? = null,
    var createdAt: LocalDateTime,
    var educationalBackground: String? = null,
    var taughtGrades: String? = null,
    var taughtSubjects: String? = null,
    var taughtCourses: String? = null,
)
