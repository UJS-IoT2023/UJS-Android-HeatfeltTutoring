package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.models.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeacherRepository : JpaRepository<Teacher, Long> {
    fun findByPhoneNumber(phoneNumber: String): Teacher?
}
