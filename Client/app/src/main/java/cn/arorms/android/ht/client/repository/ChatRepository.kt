package cn.arorms.android.ht.client.repository

import cn.arorms.android.ht.client.network.ApiService
import cn.arorms.android.ht.client.network.RetrofitClient
import cn.arorms.android.ht.client.pojo.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatRepository {
    private val apiService: ApiService = RetrofitClient.instance

    suspend fun createDialogue(participantIds: List<Long>): Result<Dialogue> {
        return try {
            withContext(Dispatchers.IO) {
                val dialogue = apiService.createDialogue(CreateDialogueRequest(participantIds))
                Result.success(dialogue)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserDialogues(userId: Long): Result<List<Dialogue>> {
        return try {
            withContext(Dispatchers.IO) {
                val dialogues = apiService.getUserDialogues(userId)
                Result.success(dialogues)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessage(dialogueId: Long, request: SendMessageRequest): Result<ChatMessage> {
        return try {
            withContext(Dispatchers.IO) {
                val message = apiService.sendMessage(dialogueId, request)
                Result.success(message)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDialogueMessages(dialogueId: Long): Result<List<ChatMessage>> {
        return try {
            withContext(Dispatchers.IO) {
                val messages = apiService.getDialogueMessages(dialogueId)
                Result.success(messages)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserMessages(): Result<List<ChatMessage>> {
        return try {
            withContext(Dispatchers.IO) {
                val messages = apiService.getUserMessages()
                Result.success(messages)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUnreadMessageCount(): Result<Map<String, Long>> {
        return try {
            withContext(Dispatchers.IO) {
                val count = apiService.getUnreadMessageCount()
                Result.success(count)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUnreadMessages(): Result<List<ChatMessage>> {
        return try {
            withContext(Dispatchers.IO) {
                val messages = apiService.getUnreadMessages()
                Result.success(messages)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markMessagesAsRead(request: MarkAsReadRequest): Result<Map<String, Int>> {
        return try {
            withContext(Dispatchers.IO) {
                val result = apiService.markMessagesAsRead(request)
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markDialogueMessagesAsRead(dialogueId: Long): Result<Map<String, Int>> {
        return try {
            withContext(Dispatchers.IO) {
                val result = apiService.markDialogueMessagesAsRead(dialogueId)
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
