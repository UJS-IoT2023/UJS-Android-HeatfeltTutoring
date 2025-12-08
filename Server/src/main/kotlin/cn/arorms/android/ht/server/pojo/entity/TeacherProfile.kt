package cn.arorms.android.ht.server.pojo.entity

import cn.arorms.android.ht.server.pojo.enums.Subject
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity @Table(name = "teachers_profile")
data class TeacherProfile (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "educational_background")
    var educationalBackground: String? = null,

    @Column(name = "taught_grades")
    var taughtGrades: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "taught_subject")
    var taughtSubject: Subject? = null,

    @Column(name = "taught_courses")
    var taughtCourses: String? = null,
)
