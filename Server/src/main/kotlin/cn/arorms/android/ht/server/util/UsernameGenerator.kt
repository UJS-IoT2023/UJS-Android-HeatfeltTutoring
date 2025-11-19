package cn.arorms.android.ht.server.util

import cn.arorms.android.ht.server.repository.UserRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class UsernameGenerator(
    private val userRepository: UserRepository
) {

    companion object {
        private const val USERNAME_PREFIX = "用户"
        private const val RANDOM_DIGITS = 6
        private const val MAX_ATTEMPTS = 10
    }

    /**
     * 生成唯一的随机用户名
     * 格式：用户 + 6位随机数字
     */
    fun generateUniqueUsername(): String {
        var attempts = 0
        while (attempts < MAX_ATTEMPTS) {
            val randomUsername = generateRandomUsername()
            if (!isUsernameExists(randomUsername)) {
                return randomUsername
            }
            attempts++
        }
        
        // 如果多次尝试后仍然冲突，使用时间戳作为后缀
        return generateRandomUsernameWithTimestamp()
    }

    /**
     * 生成随机用户名
     */
    private fun generateRandomUsername(): String {
        val random = Random()
        val randomNumber = random.nextInt(900000) + 100000 // 生成100000-999999的随机数
        return "$USERNAME_PREFIX$randomNumber"
    }

    /**
     * 生成带时间戳的用户名（用于解决冲突）
     */
    private fun generateRandomUsernameWithTimestamp(): String {
        val timestamp = System.currentTimeMillis() % 1000000 // 取时间戳后6位
        return "$USERNAME_PREFIX$timestamp"
    }

    /**
     * 检查用户名是否已存在
     */
    private fun isUsernameExists(username: String): Boolean {
        return userRepository.findByUsername(username) != null
    }
}
