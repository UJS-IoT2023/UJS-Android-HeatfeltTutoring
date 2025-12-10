package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.pojo.dto.SelectUserRequest
import cn.arorms.android.ht.server.pojo.dto.TeacherQueryRequest
import cn.arorms.android.ht.server.pojo.enums.Role
import cn.arorms.android.ht.server.pojo.entity.User
import cn.arorms.android.ht.server.repository.UserRepository
import cn.arorms.android.ht.server.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


/**
 * UserService
 * @version 1.0 2025-12-02
 * @author szh
 */
@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val walletService: WalletService,
) {
    // CRUD

    fun updateUser(id: Long, userDetails: User): User {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found with id: $id") }

        if (userDetails.password.isNotBlank()) {
            user.password = passwordEncoder.encode(userDetails.password)
        }

        return userRepository.save(user)
    }

    fun updateUserProfile(id: Long, updateDto: cn.arorms.android.ht.server.pojo.dto.UserDto): User {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found with id: $id") }

        updateDto.username?.let { user.username = it }
        updateDto.email?.let {
            if (userRepository.findByEmail(it) != null && it != user.email) {
                throw RuntimeException("邮箱已被其他用户使用")
            }
            user.email = it
        }
        updateDto.phoneNumber?.let { user.phoneNumber = it }
        updateDto.realName?.let { user.realName = it }
        updateDto.gender?.let { user.gender = it }
        updateDto.wechatId?.let { user.wechatId = it }
        updateDto.qqId?.let { user.qqId = it }
        updateDto.address?.let { user.address = it }
        updateDto.password?.let {
            if (it.isNotBlank()) {
                user.password = passwordEncoder.encode(it)
            }
        }

        // Handle teacher profile updates
        updateDto.teacherProfile?.let { teacherProfileDto ->
            val teacherProfile = user.teacherProfile ?: cn.arorms.android.ht.server.pojo.entity.TeacherProfile()
            teacherProfileDto.educationalBackground?.let { teacherProfile.educationalBackground = it }
            teacherProfileDto.taughtGrades?.let { teacherProfile.taughtGrades = it }
            teacherProfileDto.taughtSubject?.let { teacherProfile.taughtSubject = it }
            teacherProfileDto.taughtCourses?.let { teacherProfile.taughtCourses = it }
            user.teacherProfile = teacherProfile
        }

        return userRepository.save(user)
    }

    fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found with id: $id") }
        userRepository.delete(user)
    }

    fun existsById(id: Long): Boolean {
        return userRepository.existsById(id)
    }
    
    // Get users with conditions
    fun getUsers(request: SelectUserRequest? = null): List<User> {
        val allUsers = userRepository.findAll()

        if (request == null ||
            (request.userId == null &&
                    request.usernameKeyWord == null &&
                    request.role == null &&
                    request.addressKeyWord == null)) {
            return allUsers
        }
        // TODO: Specific the filter at the level of SQL
        return allUsers.filter { user ->
            (request.userId == null || user.id == request.userId) &&
                    (request.usernameKeyWord == null || user.username?.contains(request.usernameKeyWord, ignoreCase = true) == true) &&
                    (request.role == null || user.role == request.role) &&
                    (request.addressKeyWord == null || user.address?.contains(request.addressKeyWord, ignoreCase = true) == true)
        }
    }

    fun getTeacherUsers(request: TeacherQueryRequest? = null): List<User> {
        val teachers = userRepository.findAll().filter { user -> user.role == Role.TEACHER }

        if (request == null ||
            (request.keyword == null && request.subject == null)) {
            return teachers
        }

        return teachers.filter { teacher ->
            // Filter by keyword (searches across username, realName, address, educational background, taught courses)
            val keywordMatch = request.keyword?.let { keyword ->
                teacher.username?.contains(keyword, ignoreCase = true) == true ||
                teacher.realName?.contains(keyword, ignoreCase = true) == true ||
                teacher.address?.contains(keyword, ignoreCase = true) == true ||
                teacher.teacherProfile?.educationalBackground?.contains(keyword, ignoreCase = true) == true ||
                teacher.teacherProfile?.taughtCourses?.contains(keyword, ignoreCase = true) == true ||
                teacher.teacherProfile?.taughtGrades?.contains(keyword, ignoreCase = true) == true
            } ?: true

            // Filter by subject (exact match with enum)
            val subjectMatch = request.subject?.let { subject ->
                teacher.teacherProfile?.taughtSubject == subject
            } ?: true

            keywordMatch && subjectMatch
        }
    }

    // Get user by id
    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }


    
    fun getReferenceById(userId: Long): User {
        return userRepository.getReferenceById(userId)
    }
    
    // Register user
    fun registerUser(user: User): User {
        if (userRepository.findByEmail(user.email) != null) {
            throw RuntimeException("邮箱已被注册")
        }

        val encryptedPassword = passwordEncoder.encode(user.password)
        user.password = encryptedPassword

        val savedUser = userRepository.save(user)

        // Create wallet for the user
        walletService.createWallet(savedUser.id!!)
        return savedUser
    }

    // Auth methods
    fun authenticateUserByEmail(email: String, password: String): User? {
        val user = userRepository.findByEmail(email)
        return if (user != null && passwordEncoder.matches(password, user.password)) {
            user
        } else {
            null
        }
    }

    fun authenticateUserByUsername(username: String, password: String): User? {
        val user = userRepository.findByUsername(username)
        return if (user != null && passwordEncoder.matches(password, user.password)) {
            user
        } else {
            null
        }
    }

    fun authenticateUserByGoogle(googleId: String): User? {
        return userRepository.findByEmail(googleId)
    }

    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
}
