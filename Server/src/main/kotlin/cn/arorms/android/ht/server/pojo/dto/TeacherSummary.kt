package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.entity.User
import java.time.LocalDateTime

data class TeacherSummary(
    var id: Long? = null,
    var username: String,
    var email: String,
    var phoneNumber: String? = null,
    var avatarUrl: String? = null,
    var realName: String? = null,
    var gender: String? = null,
    var address: String? = null,
    var createdAt: LocalDateTime,
    var educationalBackground: String? = null,
    var taughtGrades: String? = null,
    var taughtSubjects: String? = null,
    var taughtCourses: String? = null,
) {
    constructor(user: User) : this(
        id = user.id,
        username = user.username,
        email = user.email,
        phoneNumber = user.phoneNumber,
        avatarUrl = user.avatarUrl,
        realName = user.realName,
        gender = user.gender,
        address = user.address,
        createdAt = user.createdAt,
        educationalBackground = user.teacherProfile?.educationalBackground,
        taughtGrades = user.teacherProfile?.taughtGrades,
        taughtSubjects = user.teacherProfile?.taughtSubjects,
        taughtCourses = user.teacherProfile?.taughtCourses,
    )
}
