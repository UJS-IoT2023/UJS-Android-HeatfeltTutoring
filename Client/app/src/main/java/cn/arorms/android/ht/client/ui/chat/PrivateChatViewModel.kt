package cn.arorms.android.ht.client.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.network.WebSocketService
import cn.arorms.android.ht.client.pojo.models.ChatMessage
import cn.arorms.android.ht.client.pojo.models.Dialogue
import cn.arorms.android.ht.client.pojo.models.Participant
import cn.arorms.android.ht.client.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrivateChatViewModel : ViewModel() {

    private val chatRepository = ChatRepository()

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
                val currentUserId = AuthManager.getUserId()
                val otherUserId = _otherUserId.value ?: 0
                val dialogue = Dialogue(
                    id = dialogueId,
                    participants = listOf(
                        Participant(id = currentUserId, username = null),
                        Participant(id = otherUserId, username = _otherUserName.value)
                    )
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
                        dialogue.participants?.any { it.id == currentUserId } == true &&
                        dialogue.participants?.any { it.id == otherUserId } == true &&
                        dialogue.participants?.size == 2
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
                    android.util.Log.d("ChatDebug", "Loaded ${messages.size} messages for dialogue $dialogueId")
                    if (messages.isNotEmpty()) {
                        android.util.Log.d("ChatDebug", "First message: ${messages[0]}")
                    }
                    _messages.value = messages.sortedBy { it.createdAt }

                    // Mark messages as read
                    chatRepository.markDialogueMessagesAsRead(dialogueId)
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "加载消息失败"
                    android.util.Log.e("ChatDebug", "Failed to load messages: $errorMsg")
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                android.util.Log.e("ChatDebug", "Exception loading messages: ${e.message}", e)
                _error.value = e.message ?: "加载消息失败"
            }
        }
    }

    private fun setupWebSocket(dialogueId: Long) {
        WebSocketService.connect()
        WebSocketService.subscribeToDialogue(dialogueId)

        messageListener = { message ->
            addNewMessage(message)
        }
        WebSocketService.addMessageListener(messageListener!!)
    }

    fun sendMessage(content: String) {
        val dialogueId = _dialogue.value?.id ?: return
        val currentUserId = AuthManager.getUserId()
        val currentUsername = AuthManager.getUsername()

        // Create message object for WebSocket
        val messageToSend = ChatMessage(
            dialogueId = dialogueId,
            senderId = currentUserId,
            senderUsername = currentUsername,
            content = content
        )

        // Send via WebSocket
        WebSocketService.sendMessage(dialogueId, messageToSend)

        // Optimistically add to local messages list
        _messages.value = _messages.value + messageToSend
    }

    fun addNewMessage(message: ChatMessage) {
        // Check if this message belongs to current dialogue
        val currentDialogueId = _dialogue.value?.id ?: return

        if (message.dialogueId == currentDialogueId) {
            val currentMessages = _messages.value

            // Check if this message already exists (optimistic update)
            val existingIndex = currentMessages.indexOfFirst {
                it.senderId == message.senderId &&
                it.content == message.content &&
                it.createdAt == message.createdAt
            }

            if (existingIndex >= 0) {
                // Update existing message (e.g., with server-generated ID)
                val updatedMessages = currentMessages.toMutableList()
                updatedMessages[existingIndex] = message
                _messages.value = updatedMessages.sortedBy { it.createdAt }
            } else {
                // Add new message
                _messages.value = (currentMessages + message).sortedBy { it.createdAt }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        messageListener?.let { WebSocketService.removeMessageListener(it) }
        _dialogue.value?.id?.let { WebSocketService.unsubscribeFromDialogue(it) }
    }
}
