package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.pojo.entity.Appointment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppointmentRepository : JpaRepository<Appointment, Long> {

    // TODO: Fetch entity with specific field
    fun findByUserId(userId: Long): List<Appointment>
}
