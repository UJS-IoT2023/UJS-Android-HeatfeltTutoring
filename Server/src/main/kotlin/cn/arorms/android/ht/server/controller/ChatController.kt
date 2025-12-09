package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.dto.ChatDialogueDto
import cn.arorms.android.ht.server.pojo.dto.ChatDialogueCreateRequest
import cn.arorms.android.ht.server.service.ChatDialogueService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatDialogueService: ChatDialogueService
) {

    // 获取用户的所有对话
    @GetMapping("/dialogues/{userId}")
    fun getUserDialogues(@PathVariable userId: Long): ResponseEntity<List<ChatDialogueDto>> {
        val dialogues = chatDialogueService.getUserDialogues(userId)
        return ResponseEntity.ok(dialogues)
    }

    // 创建新对话
    @PostMapping("/dialogue")
    fun createDialogue(@RequestBody request: ChatDialogueCreateRequest): ResponseEntity<Map<String, Any>> {
        val dialogue = chatDialogueService.createDialogue(request.creatorId, request.participantIds, request.title)
        return ResponseEntity.ok(mapOf(
            "dialogueId" to dialogue.id!!,
            "message" to "Dialogue created successfully"
        ))
    }

    // 获取对话的消息历史（用于查看历史消息）
    @GetMapping("/dialogue/{dialogueId}/messages")
    fun getDialogueMessages(@PathVariable dialogueId: Long): ResponseEntity<List<Map<String, Any>>> {
        val messages = chatDialogueService.getDialogueMessages(dialogueId)
        val messageDtos = messages.map { message ->
            mapOf(
                "id" to message.id!!,
                "senderId" to message.sender.id!!,
                "senderUsername" to (message.sender.username ?: "Unknown"),
                "content" to message.content,
                "createdAt" to message.createdAt
            )
        }
        return ResponseEntity.ok(messageDtos)
    }
}
