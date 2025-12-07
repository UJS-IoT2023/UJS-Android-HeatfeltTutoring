package cn.arorms.android.ht.server.pojo.dto

import java.time.LocalDateTime

data class AppointmentDto(
    var user_id: Long,
    var teacher_user_id: Long,
    var subject: String,
    var appointmentDate: LocalDateTime,
)
