package cn.arorms.android.ht.server.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity @Table(name = "teachers_profile")
data class TeacherProfile (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "educational_background")
    var educationalBackground: String,

    @Column(name = "taught_grades")
    var taughtGrades: String,

    @Column(name = "taught_courses")
    var taughtCourses: String
)