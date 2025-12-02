package cn.arorms.android.ht.server.pojo.entity

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
    var educationalBackground: String? = null,

    @Column(name = "taught_grades")
    var taughtGrades: String? = null,
    
    // TODO: Taught subjects tag table
//    @OneToMany(mappedBy = "teacherProfile")
//    @JoinColumn(name = "taught_subject_tag_id")
//    var taughtSubjects: MutableSet<TaughtSubjectTag>,
    @Column(name = "taught_subjects")
    var taughtSubjects: String? = null,

    @Column(name = "taught_courses")
    var taughtCourses: String? = null,
)