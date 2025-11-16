package cn.arorms.android.ht.server.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    var secret: String = "defaultSecretKeyThatShouldBeChangedInProductionWithAtLeast32Bytes",
    var expiration: Long = 604800000 // 7 days in milliseconds
)
