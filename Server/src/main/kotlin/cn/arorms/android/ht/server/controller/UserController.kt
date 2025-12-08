package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.dto.SelectUserRequest
import cn.arorms.android.ht.server.pojo.dto.TeacherQueryRequest
import cn.arorms.android.ht.server.pojo.dto.UserDto
import cn.arorms.android.ht.server.pojo.entity.User
import cn.arorms.android.ht.server.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Optional


@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
    private val userService: UserService
) {
    /**
     * User avatar upload to local directory
     * @param file 上传的文件
     * @param id 用户ID
     * @return 文件访问URL
     */
    @PostMapping("/upload-avatar/{id}")
    fun uploadAvatar(@RequestParam("file") file: MultipartFile, @PathVariable id: Long): ResponseEntity<Map<String, Any>> {
        return try {
            if (file.isEmpty) {
                return ResponseEntity(
                    mapOf(
                        "success" to false,
                        "message" to "文件不能为空"
                    ),
                    HttpStatus.BAD_REQUEST
                )
            }

            // 使用应用程序根目录创建上传目录
            val appDir = Paths.get(System.getProperty("user.dir"), "uploads", "avatars")
            if (!Files.exists(appDir)) {
                Files.createDirectories(appDir)
            }

            // 生成唯一文件名
            val extension = file.originalFilename?.substringAfterLast(".") ?: "jpg"
            val filename = "${id}_${System.currentTimeMillis()}.$extension"
            val filePath = appDir.resolve(filename)

            // 保存文件
            file.transferTo(filePath.toFile())

            // 生成访问URL
            val url = "/avatar/$filename"

            // 更新用户数据库
            val user = userService.getUserById(id)
                .orElseThrow { RuntimeException("用户不存在，id: $id") }

            user.avatarUrl = url
            userService.updateUser(id, user)

            ResponseEntity(
                mapOf(
                    "success" to true,
                    "message" to "头像上传成功",
                    "url" to url
                ),
                HttpStatus.OK
            )
        } catch (e: Exception) {
            ResponseEntity(
                mapOf(
                    "success" to false,
                    "message" to "头像上传失败: ${e.message}"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
    
    // Get users and support search
    @GetMapping
    fun getUsers(): ResponseEntity<List<User>> {
        return ResponseEntity(userService.getUsers(), HttpStatus.OK)
    }
    
    @PostMapping
    fun getUsers(@RequestBody selectUserRequest: SelectUserRequest? = null): ResponseEntity<List<User>> {
        return ResponseEntity(userService.getUsers(selectUserRequest), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long?): ResponseEntity<Optional<User>> {
        return if (id != null) {
            ResponseEntity(userService.getUserById(id), HttpStatus.OK)
        } else {
            ResponseEntity.badRequest().build()
        }
    }
    
    @GetMapping("/teachers")
    fun getTeachers(): ResponseEntity<List<UserDto>> {
        val teacherSummaries = userService.getTeacherUsers().map {
            teacherUser -> UserDto(teacherUser)
        }
        return ResponseEntity(teacherSummaries, HttpStatus.OK)
    }

    @PostMapping("/teachers")
    fun queryTeachers(@RequestBody request: TeacherQueryRequest): ResponseEntity<List<UserDto>> {
        val teacherSummaries = userService.getTeacherUsers(request).map {
            teacherUser -> UserDto(teacherUser)
        }
        return ResponseEntity(teacherSummaries, HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody updateDto: UserDto): ResponseEntity<User> {
        return try {
            val updatedUser = userService.updateUserProfile(id, updateDto)
            ResponseEntity(updatedUser, HttpStatus.OK)
        } catch (e: RuntimeException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
