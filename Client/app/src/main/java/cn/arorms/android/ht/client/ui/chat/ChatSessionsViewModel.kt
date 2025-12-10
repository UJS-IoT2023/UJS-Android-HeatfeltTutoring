package cn.arorms.android.ht.client.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.network.RetrofitClient
import cn.arorms.android.ht.client.pojo.models.ChatMessage
import cn.arorms.android.ht.client.pojo.models.Dialogue
import cn.arorms.android.ht.client.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatSession(
    val dialogueId: Long,
    val otherUserId: Long,
    val otherUserName: String,
    val lastMessage: String?,
    val lastMessageTime: java.time.LocalDateTime?,
    val unreadCount: Int
)

class ChatSessionsViewModel : ViewModel() {

    private val chatRepository = ChatRepository()

    private val _chatSessions = MutableStateFlow<List<ChatSession>>(emptyList())
    val chatSessions: StateFlow<List<ChatSession>> = _chatSessions

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadChatSessions()
    }

    fun loadChatSessions() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                // Get user's dialogues
                val currentUserId = AuthManager.getUserId()
                val dialoguesResult = chatRepository.getUserDialogues(currentUserId)
                if (dialoguesResult.isSuccess) {
                    val dialogues = dialoguesResult.getOrNull() ?: emptyList()

                    // Create chat sessions from dialogues (assuming private chats for now)
                    val sessions = dialogues.mapNotNull { dialogue ->
                        // For private chats, find the other participant
                        val otherParticipant = dialogue.participants?.find { it.id != currentUserId }
                        if (otherParticipant == null) return@mapNotNull null

                        val otherUserId = otherParticipant.id

                        // Get last message from dialogue
                        val lastMessage = dialogue.lastMessageContent
                        val lastMessageTime = dialogue.updatedAt

                        // Get unread count - for now assume 0, could be calculated from messages
                        val unreadCount = 0 // TODO: Calculate unread count

                        // Get user name from participant or title
                        val otherUserName = otherParticipant.username ?: dialogue.title ?: "用户 $otherUserId"

                        ChatSession(
                            dialogueId = dialogue.id!!,
                            otherUserId = otherUserId,
                            otherUserName = otherUserName,
                            lastMessage = lastMessage,
                            lastMessageTime = lastMessageTime,
                            unreadCount = unreadCount
                        )
                    }.sortedByDescending { it.lastMessageTime }

                    _chatSessions.value = sessions
                } else {
                    _error.value = dialoguesResult.exceptionOrNull()?.message ?: "加载聊天会话失败"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "加载聊天会话失败"
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
