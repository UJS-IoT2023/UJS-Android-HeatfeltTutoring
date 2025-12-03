package cn.arorms.android.ht.server.pojo.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "email_verification_codes")
data class EmailVerificationCode(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false, length = 6)
    var code: String,

    @Column(nullable = false)
    var expiresAt: LocalDateTime,

    @Column(nullable = false)
    var verified: Boolean = false,

    @Column(nullable = false)
    var attempts: Int = 0,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun isExpired(): Boolean {
        return LocalDateTime.now().isAfter(expiresAt)
    }

    fun isValid(): Boolean {
        return !isExpired() && !verified && attempts < 5
    }

    fun incrementAttempts() {
        attempts++
    }
}
