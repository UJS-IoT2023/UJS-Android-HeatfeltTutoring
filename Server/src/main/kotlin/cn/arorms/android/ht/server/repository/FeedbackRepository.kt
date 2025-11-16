package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.models.Feedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun findByUserId(userId: Long): List<Feedback>
    fun findByTeacherId(teacherId: Long): List<Feedback>
}
