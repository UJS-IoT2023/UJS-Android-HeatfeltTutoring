package cn.arorms.android.ht.client.pojo.models

import java.time.LocalDateTime

data class Appointment(
    val id: Long? = null,
    val user: User,
    val teacher: TeacherSummary,
    val subject: String,
    val appointmentDate: LocalDateTime
)
