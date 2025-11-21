package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.dto.SelectTeacherRequest
import cn.arorms.android.ht.server.models.Teacher
import cn.arorms.android.ht.server.repository.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TeacherService @Autowired constructor(
    private val teacherRepository: TeacherRepository
) {

    // 获取所有教师
    fun getAllTeachers(): List<Teacher> {
        return teacherRepository.findAll()
    }

    // 根据ID获取教师
    fun getTeacherById(id: Long): Optional<Teacher> {
        return teacherRepository.findById(id)
    }

    // 创建教师
    fun createTeacher(teacher: Teacher): Teacher {
        return teacherRepository.save(teacher)
    }

    // 更新教师
    fun updateTeacher(id: Long, teacherDetails: Teacher): Teacher {
        val teacher = teacherRepository.findById(id)
            .orElseThrow { RuntimeException("Teacher not found with id: $id") }

        teacher.phoneNumber = teacherDetails.phoneNumber
        teacher.sex = teacherDetails.sex
        teacher.name = teacherDetails.name
        teacher.icon = teacherDetails.icon
        teacher.address = teacherDetails.address
        teacher.educationalBackground = teacherDetails.educationalBackground
        teacher.taughtGrades = teacherDetails.taughtGrades

        return teacherRepository.save(teacher)
    }

    // 删除教师
    fun deleteTeacher(id: Long) {
        val teacher = teacherRepository.findById(id)
            .orElseThrow { RuntimeException("Teacher not found with id: $id") }
        teacherRepository.delete(teacher)
    }

    // 检查教师是否存在
    fun existsById(id: Long): Boolean {
        return teacherRepository.existsById(id)
    }

    fun selectTeacher(form: SelectTeacherRequest): Any {
        val course = form.course
        val grade = form.grade
       return teacherRepository.findTeachersByCourseAndGrade(course, grade)
    }
}
