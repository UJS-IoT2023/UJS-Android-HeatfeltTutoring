package cn.arorms.android.ht.server.config

import cn.arorms.android.ht.server.util.AliOssUtil
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 阿里云oss配置类
 */
@Configuration
class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    fun aliOssUtil(aliOssProperties: AliOssProperties): AliOssUtil {
        return AliOssUtil(
            aliOssProperties.endpoint,
            aliOssProperties.accessKeyId,
            aliOssProperties.accessKeySecret,
            aliOssProperties.bucketName
        )
    }
}

