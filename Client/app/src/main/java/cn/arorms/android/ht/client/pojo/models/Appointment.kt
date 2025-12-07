package cn.arorms.android.ht.client.pojo.models

import java.time.LocalDateTime

data class Appointment(
    var id: Long? = null,

    var userId: Long,
    var userName: String? = null,

    var teacherUserId: Long,
    var teacherUsername: String? = null,

    var subject: String,
    var appointmentDate: LocalDateTime,
)
