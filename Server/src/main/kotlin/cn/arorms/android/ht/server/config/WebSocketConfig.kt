package cn.arorms.android.ht.server.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        // Enable a simple in-memory message broker
        config.enableSimpleBroker("/topic", "/queue")

        // Set application destination prefix
        config.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        // Register the WebSocket endpoint
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*") // Allow connections from any origin (configure for production)
            // .withSockJS() // Disabled for wscat testing - enable for production
    }
}
