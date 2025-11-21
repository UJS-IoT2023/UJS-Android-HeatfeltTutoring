package cn.arorms.android.ht.server.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "alioss")
data class AliOssProperties(
    var endpoint: String = "",
    var accessKeyId: String = "",
    var accessKeySecret: String = "",
    var bucketName: String = ""
)

