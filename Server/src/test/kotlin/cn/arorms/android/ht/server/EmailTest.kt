package cn.arorms.android.ht.server

import cn.arorms.android.ht.server.models.EmailVerificationCode
import cn.arorms.android.ht.server.repository.EmailVerificationCodeRepository
import cn.arorms.android.ht.server.service.EmailService
import cn.arorms.android.ht.server.service.VerificationCodeService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.time.LocalDateTime

@SpringBootTest
@TestPropertySource(locations = ["classpath:application.properties"])
class EmailTest {

//    val TEST_EMAIL = "1272369577@qq.com"
    val TEST_EMAIL = "3227585869@qq.com"
    
    @Autowired
    private lateinit var emailService: EmailService
    
    @Autowired
    private lateinit var verificationCodeService: VerificationCodeService

//    @Test
//    fun simpleEmailTest() {
//        emailService.sendEmail(TEST_EMAIL, "Huangyi big ice bee")
//        emailService.sendEmail("3227585869@qq.com", "Huangyi big ice bee")
//    } 
    
    @Test
    fun sendVerificationCodeTest() {
        val verificationCode = verificationCodeService.generateVerificationCode(TEST_EMAIL)
        emailService.sendVerificationCode(TEST_EMAIL, verificationCode)
    }
    
}
