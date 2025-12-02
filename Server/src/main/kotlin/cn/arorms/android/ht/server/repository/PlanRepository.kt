package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.pojo.entity.Plan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlanRepository : JpaRepository<Plan, Long> {
//    @EntityGraph(attributePaths = ["user.id"])
    fun findByUserId(userId: Long): List<Plan>
    fun findByIsCompleted(isCompleted: Boolean): List<Plan>
}
