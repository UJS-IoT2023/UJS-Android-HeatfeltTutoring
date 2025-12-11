package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.entity.Appointment
import java.time.LocalDateTime

data class AppointmentDto(
    var id: Long? = null,

    var userId: Long? = null,
    var userName: String? = null,

    var teacherUserId: Long? = null,
    var teacherUsername: String? = null,

    var subject: String? = null,
    var appointmentDate: LocalDateTime = LocalDateTime.now(),
) {
    constructor(appointmentEntity: Appointment): this(
        id = appointmentEntity.id,
        userId = appointmentEntity.user.id!!,
        userName = appointmentEntity.user.username,
        teacherUserId = appointmentEntity.teacherUser.id!!,
        teacherUsername = appointmentEntity.teacherUser.username,
        subject = appointmentEntity.subject,
        appointmentDate = appointmentEntity.appointmentDate,
    )
}
