package cn.arorms.android.ht.server.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "teachers")
data class Teacher(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    var phoneNumber: String,
    
    var sex: String,
    
    var name: String,
    
    var icon: String,
    
    var address: String,
    
    @Column(name = "educational_background")
    var educationalBackground: String,
    
    @Column(name = "taught_grades")
    var taughtGrades: String
)