package cn.arorms.android.ht.client.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.network.WebSocketService
import cn.arorms.android.ht.client.pojo.models.ChatMessage
import cn.arorms.android.ht.client.pojo.models.Dialogue
import cn.arorms.android.ht.client.pojo.models.SendMessageRequest
import cn.arorms.android.ht.client.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrivateChatViewModel : ViewModel() {

    private val chatRepository = ChatRepository()
    private val webSocketService = WebSocketService()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _otherUserId = MutableStateFlow<Long?>(null)
    val otherUserId: StateFlow<Long?> = _otherUserId

    private val _otherUserName = MutableStateFlow<String?>(null)
    val otherUserName: StateFlow<String?> = _otherUserName

    private val _dialogue = MutableStateFlow<Dialogue?>(null)
    val dialogue: StateFlow<Dialogue?> = _dialogue

    private var messageListener: ((ChatMessage) -> Unit)? = null

    fun initialize(otherUserId: Long, otherUserName: String) {
        _otherUserId.value = otherUserId
        _otherUserName.value = otherUserName

        // First try to find existing dialogue, if not create one
        findOrCreateDialogue(otherUserId, otherUserName)
    }

    fun initializeWithDialogue(dialogueId: Long, otherUserId: Long, otherUserName: String) {
        _otherUserId.value = otherUserId
        _otherUserName.value = otherUserName

        // Load the existing dialogue directly
        loadExistingDialogue(dialogueId)
    }

    private fun loadExistingDialogue(dialogueId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                // Get dialogue details (optional, could be simplified)
                // For now, just create a placeholder dialogue object
                val dialogue = Dialogue(
                    id = dialogueId,
                    participantIds = listOf(AuthManager.getUserId(), _otherUserId.value ?: 0)
                )
                _dialogue.value = dialogue

                // Load messages and setup WebSocket
                loadMessages(dialogueId)
                setupWebSocket(dialogueId)

            } catch (e: Exception) {
                _error.value = e.message ?: "加载对话失败"
            } finally {
                _loading.value = false
            }
        }
    }

    private fun findOrCreateDialogue(otherUserId: Long, otherUserName: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                // Get user's dialogues
                val currentUserId = AuthManager.getUserId()
                val dialoguesResult = chatRepository.getUserDialogues(currentUserId)
                if (dialoguesResult.isSuccess) {
                    val dialogues = dialoguesResult.getOrNull() ?: emptyList()

                    // Find existing dialogue with this user
                    val existingDialogue = dialogues.find { dialogue ->
                        dialogue.participantIds.contains(currentUserId) &&
                        dialogue.participantIds.contains(otherUserId) &&
                        dialogue.participantIds.size == 2
                    }

                    if (existingDialogue != null) {
                        _dialogue.value = existingDialogue
                        loadMessages(existingDialogue.id!!)
                        setupWebSocket(existingDialogue.id!!)
                    } else {
                        // Create new dialogue
                        val participantIds = listOf(currentUserId, otherUserId)
                        val createResult = chatRepository.createDialogue(participantIds)
                        if (createResult.isSuccess) {
                            val newDialogue = createResult.getOrNull()
                            if (newDialogue != null) {
                                _dialogue.value = newDialogue
                                loadMessages(newDialogue.id!!)
                                setupWebSocket(newDialogue.id!!)
                            }
                        } else {
                            _error.value = createResult.exceptionOrNull()?.message ?: "创建对话失败"
                        }
                    }
                } else {
                    _error.value = dialoguesResult.exceptionOrNull()?.message ?: "获取对话列表失败"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "初始化对话失败"
            } finally {
                _loading.value = false
            }
        }
    }

    private fun loadMessages(dialogueId: Long) {
        viewModelScope.launch {
            try {
                val result = chatRepository.getDialogueMessages(dialogueId)
                if (result.isSuccess) {
                    val messages = result.getOrNull() ?: emptyList()
                    _messages.value = messages.sortedBy { it.createdAt }

                    // Mark messages as read
                    chatRepository.markDialogueMessagesAsRead(dialogueId)
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "加载消息失败"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "加载消息失败"
            }
        }
    }

    private fun setupWebSocket(dialogueId: Long) {
        webSocketService.connect()
        webSocketService.subscribeToDialogue(dialogueId)

        messageListener = { message ->
            if (message.dialogueId == dialogueId) {
                _messages.value = (_messages.value + message).sortedBy { it.createdAt }
            }
        }
        webSocketService.addMessageListener(messageListener!!)
    }

    fun sendMessage(content: String) {
        val dialogueId = _dialogue.value?.id ?: return

        viewModelScope.launch {
            try {
                val request = SendMessageRequest(content = content)
                val result = chatRepository.sendMessage(dialogueId, request)
                if (result.isSuccess) {
                    val newMessage = result.getOrNull()
                    if (newMessage != null) {
                        _messages.value = _messages.value + newMessage
                    }
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "发送消息失败"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "发送消息失败"
            }
        }
    }

    fun addNewMessage(message: ChatMessage) {
        // Check if this message belongs to current dialogue
        val currentDialogueId = _dialogue.value?.id ?: return

        if (message.dialogueId == currentDialogueId) {
            _messages.value = (_messages.value + message).sortedBy { it.createdAt }
        }
    }

    fun clearError() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        messageListener?.let { webSocketService.removeMessageListener(it) }
        _dialogue.value?.id?.let { webSocketService.unsubscribeFromDialogue(it) }
    }
}
