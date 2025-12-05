package cn.arorms.android.ht.server.config

import org.springframework.context.annotation.Configuration
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
}
