package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.dto.*
import cn.arorms.android.ht.server.models.User
import cn.arorms.android.ht.server.service.EmailService
import cn.arorms.android.ht.server.service.UserService
import cn.arorms.android.ht.server.service.VerificationCodeService
import cn.arorms.android.ht.server.util.JwtUtil
import cn.arorms.android.ht.server.util.UsernameGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(
    private val userService: UserService,
    private val verificationCodeService: VerificationCodeService,
    private val emailService: EmailService,
    private val jwtUtil: JwtUtil,
    private val usernameGenerator: UsernameGenerator
) {

    // 发送验证码
    @PostMapping("/send-verification-code")
    fun sendVerificationCode(@RequestBody request: SendVerificationCodeRequest): ResponseEntity<Map<String, String>> {
        try {
            // 检查邮箱是否已被注册
            if (userService.existsByEmail(request.email)) {
                return ResponseEntity(
                    mapOf("message" to "该邮箱已被注册"),
                    HttpStatus.BAD_REQUEST
                )
            }

            // 检查是否可以发送验证码
            if (!verificationCodeService.canSendCode(request.email)) {
                return ResponseEntity(
                    mapOf("message" to "验证码发送过于频繁，请稍后再试"),
                    HttpStatus.TOO_MANY_REQUESTS
                )
            }

            // 生成验证码
            val code = verificationCodeService.generateVerificationCode(request.email)

            // 发送邮件
            val emailSent = emailService.sendVerificationCode(request.email, code)

            if (!emailSent) {
                return ResponseEntity(
                    mapOf("message" to "验证码发送失败，请稍后重试"),
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            }

            return ResponseEntity(
                mapOf("message" to "验证码已发送到您的邮箱"),
                HttpStatus.OK
            )
        } catch (e: Exception) {
            return ResponseEntity(
                mapOf("message" to "发送验证码失败: ${e.message}"),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    // 验证邮箱
    @PostMapping("/verify-email")
    fun verifyEmail(@RequestBody request: VerifyEmailRequest): ResponseEntity<Map<String, String>> {
        try {
            val isValid = verificationCodeService.verifyCode(request.email, request.code)

            if (!isValid) {
                return ResponseEntity(
                    mapOf("message" to "验证码无效或已过期"),
                    HttpStatus.BAD_REQUEST
                )
            }

            return ResponseEntity(
                mapOf("message" to "邮箱验证成功"),
                HttpStatus.OK
            )
        } catch (e: Exception) {
            return ResponseEntity(
                mapOf("message" to "邮箱验证失败: ${e.message}"),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    // Register
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<LoginResponse> {
        try {
            // 检查邮箱是否已被注册
            if (userService.existsByEmail(registerRequest.email)) {
                return ResponseEntity(
                    LoginResponse(
                        message = "该邮箱已被注册"
                    ),
                    HttpStatus.BAD_REQUEST
                )
            }

            // 验证邮箱验证码
            if (!verificationCodeService.isEmailVerified(registerRequest.email)) {
                return ResponseEntity(
                    LoginResponse(
                        message = "请先完成邮箱验证"
                    ),
                    HttpStatus.BAD_REQUEST
                )
            }

            val user = User(
                email = registerRequest.email,
                username = registerRequest.username,
                password = registerRequest.password,
            )

            val savedUser = userService.registerUser(user)

            // 发送欢迎邮件
            emailService.sendWelcomeEmail(savedUser.email, savedUser.username)

            val token = jwtUtil.generateToken(savedUser)

            return ResponseEntity(
                LoginResponse(
                    token = token,
                    userId = savedUser.id!!,
                    username = savedUser.username,
                    message = "注册成功"
                ),
                HttpStatus.OK
            )
        } catch (e: Exception) {
            return ResponseEntity(
                LoginResponse(
                    message = "注册失败: ${e.message}"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    // Login
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        try {
            // 验证用户凭据
            val user = userService.authenticateUser(loginRequest.email, loginRequest.password)
            
            if (user == null) {
                return ResponseEntity(
                    LoginResponse(
                        message = "邮箱或密码错误"
                    ),
                    HttpStatus.UNAUTHORIZED
                )
            }

            val token = jwtUtil.generateToken(user)

            return ResponseEntity(
                LoginResponse(
                    token = token,
                    userId = user.id!!,
                    username = user.username,
                    message = "登录成功"
                ),
                HttpStatus.OK
            )
        } catch (e: Exception) {
            return ResponseEntity(
                LoginResponse(
                    message = "登录失败: ${e.message}"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    // 验证Token
    @PostMapping("/verify")
    fun verifyToken(@RequestHeader("Authorization") authHeader: String): ResponseEntity<Map<String, Any?>> {
        try {
            val token = authHeader.substring(7) // 去掉 "Bearer " 前缀
            
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity(
                    mapOf(
                        "valid" to false,
                        "message" to "Token无效或已过期"
                    ),
                    HttpStatus.UNAUTHORIZED
                )
            }

            val userId = jwtUtil.extractUserId(token)
            val claims = jwtUtil.extractAllClaims(token)

            return ResponseEntity(
                mapOf(
                    "valid" to true,
                    "userId" to userId,
                    "claims" to claims,
                    "message" to "Token验证成功"
                ),
                HttpStatus.OK
            )
        } catch (e: Exception) {
            return ResponseEntity(
                mapOf(
                    "valid" to false,
                    "message" to "Token验证失败: ${e.message}"
                ),
                HttpStatus.UNAUTHORIZED
            )
        }
    }
}
