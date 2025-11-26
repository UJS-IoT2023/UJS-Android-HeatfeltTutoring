package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.models.User
import cn.arorms.android.ht.server.repository.UserRepository
import cn.arorms.android.ht.server.util.UsernameGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val usernameGenerator: UsernameGenerator
) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    fun getUserByPhoneNumber(phoneNumber: String): User? {
        return userRepository.findByPhoneNumber(phoneNumber)
    }

    fun registerUser(user: User): User {
        if (userRepository.findByEmail(user.email) != null) {
            throw RuntimeException("邮箱已被注册")
        }

        // Generate random username
//        val randomUsername = usernameGenerator.generateUniqueUsername()
//        user.username = randomUsername

        val encryptedPassword = passwordEncoder.encode(user.password)
        user.password = encryptedPassword

        return userRepository.save(user)
    }

    // Auth and login
    fun authenticateUser(phoneNumber: String, password: String): User? {
        val user = userRepository.findByPhoneNumber(phoneNumber)
        return if (user != null && passwordEncoder.matches(password, user.password)) {
            user
        } else {
            null
        }
    }

    fun updateUser(id: Long, userDetails: User): User {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found with id: $id") }

        // 如果提供了新密码，则加密
        if (userDetails.password.isNotBlank()) {
            user.password = passwordEncoder.encode(userDetails.password)
        }

        user.phoneNumber = userDetails.phoneNumber
        user.avatarUrl = userDetails.avatarUrl
        user.address = userDetails.address

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
    
    fun existsByPhoneNumber(phoneNumber: String): Boolean {
        return userRepository.findByPhoneNumber(phoneNumber) != null
    }
}
