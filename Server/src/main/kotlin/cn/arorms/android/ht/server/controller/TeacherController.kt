package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.dto.SelectTeacherRequest
import cn.arorms.android.ht.server.models.Teacher
import cn.arorms.android.ht.server.service.TeacherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/teachers")
class TeacherController @Autowired constructor(
    private val teacherService: TeacherService
) {

    // Get all teachers
    @GetMapping("/list")
    fun getAllTeachers(): ResponseEntity<List<Teacher>> {
        val teachers = teacherService.getAllTeachers()
        return ResponseEntity.ok(teachers)
    }

    // Get teacher by id
    @GetMapping("/{id}")
    fun getTeacherById(@PathVariable id: Long): ResponseEntity<Teacher> {
        val teacher = teacherService.getTeacherById(id)
        return if (teacher.isPresent) {
            ResponseEntity.ok(teacher.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // Create a teacher profile
    @PostMapping
    fun createTeacher(@RequestBody teacher: Teacher): ResponseEntity<Teacher> {
        val createdTeacher = teacherService.createTeacher(teacher)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher)
    }

    
    
    // Update a teacher profile
    @PutMapping("/{id}")
    fun updateTeacher(@PathVariable id: Long, @RequestBody teacherDetails: Teacher): ResponseEntity<Teacher> {
        try {
            val updatedTeacher = teacherService.updateTeacher(id, teacherDetails)
            return ResponseEntity.ok(updatedTeacher)
        } catch (e: RuntimeException) {
            return ResponseEntity.notFound().build()
        }
    }

    // Delete a teacher profile
    @DeleteMapping("/{id}")
    fun deleteTeacher(@PathVariable id: Long): ResponseEntity<Void> {
        try {
            teacherService.deleteTeacher(id)
            return ResponseEntity.noContent().build()
        } catch (e: RuntimeException) {
            return ResponseEntity.notFound().build()
        }
    }
    @GetMapping
    fun selectTeacher(@ModelAttribute form: SelectTeacherRequest): ResponseEntity<Any> {
        try {
            val selectTeacher = teacherService.selectTeacher(form)
            return ResponseEntity.ok(selectTeacher)
        } catch (e: RuntimeException) {
            return ResponseEntity.notFound().build()
        }
    }

}
