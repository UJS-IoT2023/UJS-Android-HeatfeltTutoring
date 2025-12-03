//package cn.arorms.android.ht.server.models
//
//import cn.arorms.android.ht.server.enums.Subject
//import jakarta.persistence.Entity
//import jakarta.persistence.EnumType
//import jakarta.persistence.Enumerated
//import jakarta.persistence.GeneratedValue
//import jakarta.persistence.GenerationType
//import jakarta.persistence.Id
//import jakarta.persistence.JoinColumn
//import jakarta.persistence.ManyToOne
//import jakarta.persistence.Table
//
//@Entity
//@Table()
//data class TaughtSubjectTag (
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    var id: Long? = null,
//    
//    @Enumerated(EnumType.STRING)
//    var subject: Subject,
//
//    @ManyToOne
//    @JoinColumn(name = "teacher_profile_id", nullable = false)
//    val teacherProfile: TeacherProfile? = null
//)