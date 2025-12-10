package cn.arorms.android.ht.server.pojo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity @Table(name = "plan")
data class Plan (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    var user: User,

    var content: String,
    
    var deadline: LocalDateTime,
    
    @Column(name = "is_completed")
    var isCompleted: Boolean,
)