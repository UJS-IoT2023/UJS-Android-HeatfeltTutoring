package cn.arorms.android.ht.server.pojo.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity @Table(name = "appointments")
data class Appointment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne
    @JoinColumn(name = "teacher_user_id")
    var teacherUser: User,

    var subject: String,

    @Column(name = "appointment_date")
    var appointmentDate: LocalDateTime,
)