package cn.arorms.android.ht.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.file.Paths

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // Map static resource api /avatar/** to uploads/avatars/ directory
        val avatarPath = Paths.get(System.getProperty("user.dir"), "uploads", "avatars").toAbsolutePath().toString()

        registry.addResourceHandler("/avatars/**")
            .addResourceLocations("file:$avatarPath/")
    }

    override fun configureAsyncSupport(configurer: AsyncSupportConfigurer) {
        configurer.setTaskExecutor(asyncTaskExecutor())
    }

    @Bean
    fun asyncTaskExecutor(): AsyncTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 10
        executor.maxPoolSize = 50
        executor.queueCapacity = 100
        executor.setThreadNamePrefix("async-executor-")
        executor.initialize()
        return executor
    }
}
