package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.pojo.dto.AgentRequest
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class AgentService @Autowired constructor(
    private val chatClient: ChatClient
) {
    /**
     * Generate AI response, return when finished
     */
    fun generate(generateRequest: AgentRequest): String? {
        return chatClient.prompt()
            .user(generateRequest.message)
            .call()
            .content()
    }

    /**
     * Stream response
     */
    fun chat(message: String, conversationId: String? = null): Flux<String> {
        return chatClient.prompt()
            .user(message)
            .stream()
            .content()
    }
}
