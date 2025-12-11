package cn.arorms.android.ht.server.pojo.entity

import cn.arorms.android.ht.server.pojo.enums.Role
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.EntityGraph
import java.time.LocalDateTime

@Entity @Table(name = "users")
data class User(

    // ===== Auth information =====
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "username", unique = true)
    var username: String,

    @Column(unique = true)
    var email: String,

    var password: String,
    
    // ===== Extended info =====
    @Enumerated(EnumType.STRING)
    var role: Role? = Role.STUDENT,
    
    @Column(name = "google_id")
    var googleId: String? = null,
    
    @Column(name = "wechat_openid")
    var wechatOpenid: String? = null,

    @Column(name = "qq_openid")
    var qqOpenid: String? = null,

    @OneToOne
    @JoinColumn(name = "teacher_profile_id")
    var teacherProfile: TeacherProfile? = null,


    // ===== Profile info =====
    @Column(name = "phone_number", unique = true, length = 20, nullable = true)
    var phoneNumber: String? = null,

    @Column(name = "avatar_url")
    var avatarUrl: String? = null,

    @Column(name = "real_name")
    var realName: String? = null,
    
    var gender: String? = null,

    @Column(name = "wechat_id", unique = true)
    var wechatId: String? = null,
    
    @Column(name =  "qq_id", unique = true)
    var qqId: String? = null,
    
    var address: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        @EntityGraph(attributePaths = ["id", "username"])
        @JvmStatic
        fun basicProjection() = this
    }
}
