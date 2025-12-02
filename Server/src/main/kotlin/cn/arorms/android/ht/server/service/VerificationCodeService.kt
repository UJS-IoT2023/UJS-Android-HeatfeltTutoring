package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.pojo.entity.EmailVerificationCode
import cn.arorms.android.ht.server.repository.EmailVerificationCodeRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class VerificationCodeService @Autowired constructor(
    private val emailVerificationCodeRepository: EmailVerificationCodeRepository
) {
    companion object {
        private const val CODE_LENGTH = 6
        private const val CODE_EXPIRY_MINUTES = 5
        private const val MAX_ATTEMPTS = 5
    }
    
    @Transactional
    fun generateVerificationCode(email: String): String {
//        // 清理过期的验证码
//        emailVerificationCodeRepository.deleteExpiredCodes(LocalDateTime.now())
        
        // 删除该邮箱之前的验证码
        emailVerificationCodeRepository.deleteByEmail(email)
        
        // 生成6位数字验证码
        val code = (100000..999999).random().toString()
        
        // 创建验证码记录
        val verificationCode = EmailVerificationCode(
            email = email,
            code = code,
            expiresAt = LocalDateTime.now().plusMinutes(CODE_EXPIRY_MINUTES.toLong())
        )
        
        emailVerificationCodeRepository.save(verificationCode)
        
        return code
    }

    fun verifyCode(email: String, code: String): Boolean {
        val verificationCode = emailVerificationCodeRepository.findByEmailAndCode(email, code)
        
        if (verificationCode == null || !verificationCode.isValid()) {
            return false
        }
        
        // 验证成功，标记为已验证
        verificationCode.verified = true
        emailVerificationCodeRepository.save(verificationCode)
        
        return true
    }

    fun validateCode(email: String, code: String): Boolean {
        val verificationCode = emailVerificationCodeRepository.findByEmailAndCode(email, code)
        
        if (verificationCode == null || !verificationCode.isValid()) {
            // 增加尝试次数
            verificationCode?.let {
                it.incrementAttempts()
                emailVerificationCodeRepository.save(it)
            }
            return false
        }
        
        return true
    }

    fun isEmailVerified(email: String): Boolean {
        val verificationCode = emailVerificationCodeRepository.findByEmail(email)
        return verificationCode?.verified == true
    }

    fun canSendCode(email: String): Boolean {
        val existingCode = emailVerificationCodeRepository.findByEmail(email)
        return existingCode == null || existingCode.isExpired()
    }

//    fun cleanupExpiredCodes() {
//        emailVerificationCodeRepository.deleteExpiredCodes(LocalDateTime.now())
//    }
}
