package cn.arorms.android.ht.client.models

import java.time.LocalDateTime

data class Appointment(
    val id: Long? = null,
    val user: User,
    val teacher: Teacher,
    val subject: String,
    val appointmentDate: LocalDateTime
)
