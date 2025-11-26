package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.dto.*
import cn.arorms.android.ht.server.models.User
import cn.arorms.android.ht.server.service.UserService
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
    private val jwtUtil: JwtUtil,
    private val usernameGenerator: UsernameGenerator
) {

    // Register
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<LoginResponse> {
        try {
            if (userService.existsByEmail(registerRequest.email)) {
                return ResponseEntity(
                    LoginResponse(
                        message = "手机号已被注册"
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

            val token = jwtUtil.generateToken(savedUser)

            return ResponseEntity(
                LoginResponse(
                    token = token,
                    userId = savedUser.id!!,
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
            val user = userService.authenticateUser(loginRequest.username, loginRequest.password)
            
            if (user == null) {
                return ResponseEntity(
                    LoginResponse(
                        message = "手机号或密码错误"
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
