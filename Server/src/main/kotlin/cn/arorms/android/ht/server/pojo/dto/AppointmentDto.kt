package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.entity.Appointment
import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime

data class AppointmentDto @JsonCreator constructor(
    var id: Long? = null,
    
    var userId: Long,
    var userName: String? = null,
    
    var teacherUserId: Long,
    var teacherUsername: String? = null,
    
    var subject: String,
    var appointmentDate: LocalDateTime,
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
