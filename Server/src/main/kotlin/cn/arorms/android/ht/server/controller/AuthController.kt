package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.dto.LoginRequest
import cn.arorms.android.ht.server.pojo.dto.LoginResponse
import cn.arorms.android.ht.server.pojo.dto.RegisterRequest
import cn.arorms.android.ht.server.pojo.dto.SendVerificationCodeRequest
import cn.arorms.android.ht.server.pojo.dto.VerifyEmailRequest
import cn.arorms.android.ht.server.pojo.entity.User
import cn.arorms.android.ht.server.pojo.enums.LoginType
import cn.arorms.android.ht.server.pojo.enums.RegisterType
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

    // Send verification code
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

    // Verify the email code
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
            if (!verificationCodeService.isEmailVerified(registerRequest.email) && registerRequest.registerType == RegisterType.EMAIL) {
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
            val user = when(loginRequest.loginType) {
                LoginType.USERNAME -> userService.authenticateUserByUsername(loginRequest.identifier, loginRequest.password)
                LoginType.EMAIL -> userService.authenticateUserByEmail(loginRequest.identifier, loginRequest.password)
                LoginType.GOOGLE -> userService.authenticateUserByGoogle(loginRequest.identifier)
            }
            
            if (user == null) {
                return ResponseEntity(
                    LoginResponse(message = "Error identifier or password."),
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

//    // OAuth2 登录成功回调
//    @GetMapping("/oauth2/success")
//    fun oauth2Success(@RequestParam("code") code: String? = null): ResponseEntity<LoginResponse> {
//        try {
//            // 注意：这个端点实际上不会被直接调用
//            // Spring Security OAuth2 会在认证成功后重定向到这里
//            // 在实际应用中，前端应该从重定向URL中获取token
//            // 这里我们返回一个指示，告诉前端如何获取token
//            
//            return ResponseEntity(
//                LoginResponse(
//                    message = "微信登录成功，请使用获取的code调用/login/oauth2/token端点获取JWT token"
//                ),
//                HttpStatus.OK
//            )
//        } catch (e: Exception) {
//            return ResponseEntity(
//                LoginResponse(
//                    message = "OAuth2登录处理失败: ${e.message}"
//                ),
//                HttpStatus.INTERNAL_SERVER_ERROR
//            )
//        }
//    }
//
//    // 获取OAuth2登录的JWT token
//    @PostMapping("/oauth2/token")
//    fun getOAuth2Token(@RequestBody request: Map<String, String>): ResponseEntity<LoginResponse> {
//        try {
//            val wechatOpenid = request["wechatOpenid"]
//            
//            if (wechatOpenid.isNullOrEmpty()) {
//                return ResponseEntity(
//                    LoginResponse(message = "微信openid不能为空"),
//                    HttpStatus.BAD_REQUEST
//                )
//            }
//
//            // 查找用户
//            val user = userService.authenticateUserByWechat(wechatOpenid, "")
//            
//            if (user == null) {
//                return ResponseEntity(
//                    LoginResponse(message = "用户不存在，请先通过微信登录"),
//                    HttpStatus.UNAUTHORIZED
//                )
//            }
//
//            val token = jwtUtil.generateToken(user)
//
//            return ResponseEntity(
//                LoginResponse(
//                    token = token,
//                    userId = user.id!!,
//                    username = user.username,
//                    message = "Token获取成功"
//                ),
//                HttpStatus.OK
//            )
//        } catch (e: Exception) {
//            return ResponseEntity(
//                LoginResponse(
//                    message = "获取Token失败: ${e.message}"
//                ),
//                HttpStatus.INTERNAL_SERVER_ERROR
//            )
//        }
//    }
}
