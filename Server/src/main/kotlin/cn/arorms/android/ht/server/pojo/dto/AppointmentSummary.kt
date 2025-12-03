package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.entity.Appointment
import java.time.LocalDateTime

data class AppointmentSummary(
    var user_real_name: String? = null,
    var teacher_real_name: String? = null,
    var subject: String,
    var appointmentDate: LocalDateTime,
) {
    constructor(appointmentEntity: Appointment): this(
        subject = appointmentEntity.subject,
        appointmentDate = appointmentEntity.appointmentDate,
        user_real_name = appointmentEntity.user.realName,
        teacher_real_name = appointmentEntity.teacherUser.realName,
    )
}
