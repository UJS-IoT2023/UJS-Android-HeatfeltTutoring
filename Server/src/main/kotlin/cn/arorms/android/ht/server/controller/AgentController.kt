package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.dto.AgentRequest
import cn.arorms.android.ht.server.service.AgentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/ai")
class AgentController @Autowired constructor(
    private val aiService: AgentService
) {
    @PostMapping("/generate")
    fun generate(@RequestBody request: AgentRequest): ResponseEntity<String> {
        try {
            val response = aiService.generate(request)
            return ResponseEntity.ok(response)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
    }

    @PostMapping("/chat", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun chatWithAIStream(@RequestBody request: AgentRequest): Flux<String> {
        return aiService.chat(request.message, request.conversationId)
    }
}
