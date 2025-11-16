package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.dto.*
import cn.arorms.android.ht.server.models.User
import cn.arorms.android.ht.server.service.UserService
import cn.arorms.android.ht.server.util.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController @Autowired constructor(
    private val userService: UserService,
    private val jwtUtil: JwtUtil
) {

    // 用户注册
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<AuthResponse> {
        try {
            // 检查手机号是否已存在
            if (userService.existsByPhoneNumber(registerRequest.phoneNumber)) {
                return ResponseEntity(
                    AuthResponse(
                        token = "",
                        userId = 0,
                        phoneNumber = "",
                        message = "手机号已被注册"
                    ),
                    HttpStatus.BAD_REQUEST
                )
            }

            // 创建用户对象
            val user = User(
                phoneNumber = registerRequest.phoneNumber,
                password = registerRequest.password, // 会在service层加密
                icon = registerRequest.icon,
                address = registerRequest.address
            )

            // 注册用户
            val savedUser = userService.registerUser(user)

            // 生成JWT Token
            val token = jwtUtil.generateToken(savedUser)

            return ResponseEntity(
                AuthResponse(
                    token = token,
                    userId = savedUser.id!!,
                    phoneNumber = savedUser.phoneNumber,
                    icon = savedUser.icon,
                    address = savedUser.address,
                    message = "注册成功"
                ),
                HttpStatus.OK
            )
        } catch (e: Exception) {
            return ResponseEntity(
                AuthResponse(
                    token = "",
                    userId = 0,
                    phoneNumber = "",
                    message = "注册失败: ${e.message}"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    // 用户登录
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        try {
            // 验证用户凭据
            val user = userService.authenticateUser(loginRequest.phoneNumber, loginRequest.password)
            
            if (user == null) {
                return ResponseEntity(
                    AuthResponse(
                        token = "",
                        userId = 0,
                        phoneNumber = "",
                        message = "手机号或密码错误"
                    ),
                    HttpStatus.UNAUTHORIZED
                )
            }

            // 生成JWT Token
            val token = jwtUtil.generateToken(user)

            return ResponseEntity(
                AuthResponse(
                    token = token,
                    userId = user.id!!,
                    phoneNumber = user.phoneNumber,
                    icon = user.icon,
                    address = user.address,
                    message = "登录成功"
                ),
                HttpStatus.OK
            )
        } catch (e: Exception) {
            return ResponseEntity(
                AuthResponse(
                    token = "",
                    userId = 0,
                    phoneNumber = "",
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
