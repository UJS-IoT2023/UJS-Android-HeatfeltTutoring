package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.entity.TeacherProfile
import cn.arorms.android.ht.server.pojo.enums.Subject

data class TeacherProfileDto(
    var id: Long? = null,
    var educationalBackground: String? = null,
    var taughtGrades: String? = null,
    var taughtSubject: Subject? = null,
    var taughtCourses: String? = null,
) {
    constructor(teacherProfile: TeacherProfile) : this(
        id = teacherProfile.id,
        educationalBackground = teacherProfile.educationalBackground,
        taughtGrades = teacherProfile.taughtGrades,
        taughtSubject = teacherProfile.taughtSubject,
        taughtCourses = teacherProfile.taughtCourses,
    )
}
