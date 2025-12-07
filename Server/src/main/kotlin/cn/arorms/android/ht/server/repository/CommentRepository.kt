package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.pojo.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import kotlin.collections.List

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByToUserId(fromUserId: Long): List<Comment>
}
