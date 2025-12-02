package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.dto.SelectUserRequest
import cn.arorms.android.ht.server.pojo.entity.User
import cn.arorms.android.ht.server.service.UserService
import cn.arorms.android.ht.server.util.AliOssUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
    private val aliOssUtil: AliOssUtil,
    private val userService: UserService
) {
    /**
     * User avatar upload
     * TODO: Redirect the avatar upload api
     * @param file 上传的文件
     * @param id 用户ID
     * @return 文件访问URL
     */
    @PostMapping("/upload-avatar/{id}")
    fun uploadAvatar(file: MultipartFile, @PathVariable id: Long): ResponseEntity<Map<String, Any>> {
        return try {
            // 检查文件是否为空
            if (file.isEmpty) {
                return ResponseEntity(
                    mapOf(
                        "success" to false,
                        "message" to "文件不能为空"
                    ),
                    HttpStatus.BAD_REQUEST
                )
            }

            // 获取原始文件名
            val originalFilename = file.originalFilename
            if (originalFilename.isNullOrBlank()) {
                return ResponseEntity(
                    mapOf(
                        "success" to false,
                        "message" to "文件名不能为空"
                    ),
                    HttpStatus.BAD_REQUEST
                )
            }

            // 读取文件字节
            val bytes = file.bytes

            // 上传到OSS
            val url = aliOssUtil.upload(bytes, originalFilename)
            
            // 存储到数据库 - 更新用户的icon字段
            val user = userService.getUserById(id)
                .orElseThrow { RuntimeException("用户不存在，id: $id") }

            user.avatarUrl = url
            userService.updateUser(id, user)
            ResponseEntity(
                mapOf(
                    "success" to true,
                    "message" to "文件上传成功",
                    "url" to url,
                    "fileName" to originalFilename
                ),
                HttpStatus.OK
            )
        } catch (e: Exception) {
            ResponseEntity(
                mapOf(
                    "success" to false,
                    "message" to "文件上传失败: ${e.message}"
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
    
    @GetMapping("/teachers")
    fun getTeachers(): ResponseEntity<List<User>> {
        val teachers = userService.getTeacherUsers()
        return ResponseEntity(teachers, HttpStatus.OK)
    }
}

