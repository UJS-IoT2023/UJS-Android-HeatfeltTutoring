package cn.arorms.android.ht.server.config

import cn.arorms.android.ht.server.service.UserService
import cn.arorms.android.ht.server.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
    private val jwtUtil: JwtUtil,
    private val userService: UserService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            try {
                if (jwtUtil.validateToken(token)) {
                    val userId = jwtUtil.extractUserId(token)

                    if (userId != null) {
                        val user = userService.getUserById(userId).orElse(null)

                        if (user != null) {
                            val authentication = UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                emptyList()
                            )
                            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                            SecurityContextHolder.getContext().authentication = authentication
                        }
                    }
                }
            } catch (e: Exception) {
                logger.debug("JWT token validation failed: ${e.message}")
            }
        }

        filterChain.doFilter(request, response)
    }
}