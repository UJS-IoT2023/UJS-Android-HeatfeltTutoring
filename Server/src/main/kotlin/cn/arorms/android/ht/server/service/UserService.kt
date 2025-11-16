package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.models.User
import cn.arorms.android.ht.server.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    // 获取所有用户
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    // 根据ID获取用户
    fun getUserById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    // 根据手机号获取用户
    fun getUserByPhoneNumber(phoneNumber: String): User? {
        return userRepository.findByPhoneNumber(phoneNumber)
    }

    // 注册用户 - 密码会自动加密
    fun registerUser(user: User): User {
        // 检查手机号是否已存在
        if (userRepository.findByPhoneNumber(user.phoneNumber) != null) {
            throw RuntimeException("手机号已被注册")
        }

        // 加密密码
        val encryptedPassword = passwordEncoder.encode(user.password)
        user.password = encryptedPassword

        return userRepository.save(user)
    }

    // 验证用户登录
    fun authenticateUser(phoneNumber: String, password: String): User? {
        val user = userRepository.findByPhoneNumber(phoneNumber)
        return if (user != null && passwordEncoder.matches(password, user.password)) {
            user
        } else {
            null
        }
    }

    // 更新用户信息
    fun updateUser(id: Long, userDetails: User): User {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found with id: $id") }

        // 如果提供了新密码，则加密
        if (userDetails.password.isNotBlank()) {
            user.password = passwordEncoder.encode(userDetails.password)
        }

        user.phoneNumber = userDetails.phoneNumber
        user.icon = userDetails.icon
        user.address = userDetails.address

        return userRepository.save(user)
    }

    // 删除用户
    fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found with id: $id") }
        userRepository.delete(user)
    }

    // 检查用户是否存在
    fun existsById(id: Long): Boolean {
        return userRepository.existsById(id)
    }

    // 检查手机号是否存在
    fun existsByPhoneNumber(phoneNumber: String): Boolean {
        return userRepository.findByPhoneNumber(phoneNumber) != null
    }
}
