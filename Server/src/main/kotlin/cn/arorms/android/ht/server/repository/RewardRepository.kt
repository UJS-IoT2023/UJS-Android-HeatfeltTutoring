package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.pojo.entity.Reward
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RewardRepository : JpaRepository<Reward, Long> {
    fun findByUserId(userId: Long): List<Reward>
}
