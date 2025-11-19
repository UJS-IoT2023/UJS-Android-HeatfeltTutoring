package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.models.Appointment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface AppointmentRepository : JpaRepository<Appointment, Long> {
    
    // 投影接口 - 只包含需要的字段
    interface AppointmentProjection {
        fun getId(): Long?
        fun getSubject(): String
        fun getAppointmentDate(): LocalDateTime
        fun getUsername(): String?
        fun getTeacherName(): String?
    }
    
    /**
     * 查询所有预约，包含用户名和教师名
     */
    @Query("""
        SELECT 
            a.id as id,
            a.subject as subject,
            a.appointmentDate as appointmentDate,
            u.username as username,
            t.name as teacherName
        FROM Appointment a
        LEFT JOIN a.user u
        LEFT JOIN a.teacher t
    """)
    fun findAllWithUserAndTeacherNames(): List<AppointmentProjection>
    
    /**
     * 根据用户ID查询预约，包含用户名和教师名
     */
    @Query("""
        SELECT 
            a.id as id,
            a.subject as subject,
            a.appointmentDate as appointmentDate,
            u.username as username,
            t.name as teacherName
        FROM Appointment a
        LEFT JOIN a.user u
        LEFT JOIN a.teacher t
        WHERE a.user.id = :userId
    """)
    fun findByUserIdWithNames(userId: Long): List<AppointmentProjection>
    
    /**
     * 根据教师ID查询预约，包含用户名和教师名
     */
    @Query("""
        SELECT 
            a.id as id,
            a.subject as subject,
            a.appointmentDate as appointmentDate,
            u.username as username,
            t.name as teacherName
        FROM Appointment a
        LEFT JOIN a.user u
        LEFT JOIN a.teacher t
        WHERE a.teacher.id = :teacherId
    """)
    fun findByTeacherIdWithNames(teacherId: Long): List<AppointmentProjection>
    
    /**
     * 根据ID查询单个预约，包含用户名和教师名
     */
    @Query("""
        SELECT 
            a.id as id,
            a.subject as subject,
            a.appointmentDate as appointment_date,
            u.username as username,
            t.name as teacherName
        FROM Appointment a
        LEFT JOIN a.user u
        LEFT JOIN a.teacher t
        WHERE a.id = :id
    """)
    fun findByIdWithNames(id: Long): AppointmentProjection?
    
    // 原有的方法保持不变，用于返回完整实体
    fun findByUserId(userId: Long): List<Appointment>
    fun findByTeacherId(teacherId: Long): List<Appointment>
}
