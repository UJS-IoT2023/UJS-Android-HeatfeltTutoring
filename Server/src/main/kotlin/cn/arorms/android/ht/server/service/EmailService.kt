package cn.arorms.android.ht.server.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {
    
    // Logger
    companion object {
        private val logger = LoggerFactory.getLogger(EmailService::class.java)
    }

    @Value("\${spring.mail.username}")
    private lateinit var fromEmail: String

    // A simple email sender function
    fun sendEmail(email: String, content: String): Boolean{
        try {
            val message = SimpleMailMessage()
            message.setFrom(fromEmail)
            message.setTo(email)
            message.subject = "Heartfelt Tutoring - 自定义邮件"
            message.text = """
                亲爱的用户：
                
                $content
                
                祝您使用愉快！
                Arorms 团队
            """.trimIndent()

            mailSender.send(message)
            logger.info("验证码邮件已发送到: $email")
            return true
        } catch (e: Exception) {
            logger.error("发送验证码邮件失败: ${e.message}", e)
            return false
        }
    }
    
    fun sendVerificationCode(email: String, code: String): Boolean {
        try {
            val message = SimpleMailMessage()
            message.setFrom(fromEmail)
            message.setTo(email)
            message.subject = "Heartfelt Tutoring - 邮箱验证码"
            message.text = """
                亲爱的用户：
                
                您的邮箱验证码是：$code
                
                验证码有效期为5分钟，请尽快完成验证。
                
                如果您没有请求此验证码，请忽略此邮件。
                
                祝您使用愉快！
                Arorms 团队
            """.trimIndent()

            mailSender.send(message)
            logger.info("验证码邮件已发送到: $email")
            return true
        } catch (e: Exception) {
            logger.error("发送验证码邮件失败: ${e.message}", e)
            return false
        }
    }

    fun sendWelcomeEmail(email: String, username: String): Boolean {
        try {
            val message = SimpleMailMessage()
            message.setFrom(fromEmail)
            message.setTo(email)
            message.subject = "欢迎加入 Heartfelt Tutoring"
            message.text = """
                亲爱的 $username：
                
                欢迎您加入 Heartfelt Tutoring！
                
                您的账户已成功创建，现在可以开始使用我们的服务。
                
                如果您有任何问题，请随时联系我们。
                
                祝您使用愉快！
                Arorms 团队
            """.trimIndent()

            mailSender.send(message)
            logger.info("欢迎邮件已发送到: $email")
            return true
        } catch (e: Exception) {
            logger.error("发送欢迎邮件失败: ${e.message}", e)
            return false
        }
    }
}
