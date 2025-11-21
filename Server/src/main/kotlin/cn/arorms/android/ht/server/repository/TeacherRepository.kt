package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.models.Teacher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TeacherRepository : JpaRepository<Teacher, Long> {
    fun findByPhoneNumber(phoneNumber: String): Teacher?

    @Query("SELECT t FROM Teacher t WHERE " +
            "(:course IS NULL OR :course = '' OR t.taughtCourses LIKE %:course%) AND " +
            "(:grade IS NULL OR :grade = '' OR t.taughtGrades LIKE %:grade%)")
    fun findTeachersByCourseAndGrade(
        @Param("course") course: String?,
        @Param("grade") grade: String?
    ): List<Teacher>
}
