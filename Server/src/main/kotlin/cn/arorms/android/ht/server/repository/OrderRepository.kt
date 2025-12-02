package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.pojo.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findByUserId(userId: Long): List<Order>
    fun findByState(state: String): List<Order>
}
