package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.models.Appointment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppointmentRepository : JpaRepository<Appointment, Long> {
    fun findByUserId(userId: Long): List<Appointment>
    fun findByTeacherId(teacherId: Long): List<Appointment>
}
