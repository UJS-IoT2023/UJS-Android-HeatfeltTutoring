package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.models.EmailVerificationCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface EmailVerificationCodeRepository : JpaRepository<EmailVerificationCode, Long> {
    
    fun findByEmailAndCode(email: String, code: String): EmailVerificationCode?
    
    fun findByEmail(email: String): EmailVerificationCode?
    
//    @Modifying
//    @Query("DELETE FROM EmailVerificationCode e WHERE e.expiresAt < :currentTime")
//    fun deleteExpiredCodes(@Param("currentTime") currentTime: LocalDateTime)
    
    @Modifying
    @Query("DELETE FROM EmailVerificationCode e WHERE e.email = :email")
    fun deleteByEmail(@Param("email") email: String)
}
