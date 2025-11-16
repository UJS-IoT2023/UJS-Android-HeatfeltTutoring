package cn.arorms.android.ht.server.util

import cn.arorms.android.ht.server.config.JwtProperties
import cn.arorms.android.ht.server.models.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(
    private val jwtProperties: JwtProperties
) {

    private val key = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())

    /**
     * Generate JWT token for user
     */
    fun generateToken(user: User): String {
        return Jwts.builder()
            .subject(user.id.toString())
            .claim("phoneNumber", user.phoneNumber)
            .claim("icon", user.icon)
            .claim("address", user.address)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + jwtProperties.expiration))
            .signWith(key)
            .compact()
    }

    /**
     * Extract user ID from token
     */
    fun extractUserId(token: String): Long? {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
            
            claims.subject.toLong()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Validate token
     */
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Extract all claims from token
     */
    fun extractAllClaims(token: String): Map<String, Any>? {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
            
            claims
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Check if token is expired
     */
    fun isTokenExpired(token: String): Boolean {
        return try {
            val expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
                .expiration
            
            expiration.before(Date())
        } catch (e: Exception) {
            true
        }
    }
}
