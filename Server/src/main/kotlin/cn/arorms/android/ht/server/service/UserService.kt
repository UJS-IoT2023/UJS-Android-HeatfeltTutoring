package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.dto.SelectUserRequest
import cn.arorms.android.ht.server.enums.Role
import cn.arorms.android.ht.server.models.User
import cn.arorms.android.ht.server.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

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

    fun getTeacherUsers(): List<User> {
         return userRepository.findAll().filter {
             user -> user.role == Role.TEACHER
         }
    }
    
    // Get user by id
    fun getUserById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    // Register user
    fun registerUser(user: User): User {
        if (userRepository.findByEmail(user.email) != null) {
            throw RuntimeException("邮箱已被注册")
        }

        val encryptedPassword = passwordEncoder.encode(user.password)
        user.password = encryptedPassword

        return userRepository.save(user)
    }

    // Auth and login
    fun authenticateUser(email: String, password: String): User? {
        val user = userRepository.findByEmail(email)
        return if (user != null && passwordEncoder.matches(password, user.password)) {
            user
        } else {
            null
        }
    }

    fun updateUser(id: Long, userDetails: User): User {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found with id: $id") }

        if (userDetails.password.isNotBlank()) {
            user.password = passwordEncoder.encode(userDetails.password)
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

    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
}
