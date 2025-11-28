package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}
