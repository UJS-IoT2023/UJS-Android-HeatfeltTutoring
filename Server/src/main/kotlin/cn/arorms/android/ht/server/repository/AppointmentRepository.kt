package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.models.Appointment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AppointmentRepository : JpaRepository<Appointment, Long> {

    // TODO: Fetch entity with specific field
    fun findByUserId(userId: Long): List<Appointment>
}
