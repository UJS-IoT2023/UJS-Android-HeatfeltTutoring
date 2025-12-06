package cn.arorms.android.ht.server.config

import cn.arorms.android.ht.server.service.UserService
import cn.arorms.android.ht.server.util.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
    private val userService: UserService,
    private val jwtUtil: JwtUtil
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oauth2User = authentication.principal as OAuth2User
        val googleId = oauth2User.attributes["sub"] as String
        val email = oauth2User.attributes["email"] as String
        val name = oauth2User.attributes["name"] as String

        // Check if user exists by Google ID
        var user = userService.authenticateUserByGoogle(googleId)

        if (user == null) {
            // User doesn't exist, create new user
            val newUser = cn.arorms.android.ht.server.pojo.entity.User(
                username = name,
                email = email,
                password = "", // OAuth users don't need password
                googleId = googleId
            )
            user = userService.registerUser(newUser)
        }

        // Generate JWT token
        val token = jwtUtil.generateToken(user)

        // Return JWT token as JSON
        response.contentType = "application/json"
        response.writer.write("""{"token": "$token", "userId": ${user.id}, "username": "${user.username}", "message": "Google login successful"}""")
    }
}
