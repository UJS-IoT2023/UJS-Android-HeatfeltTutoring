package cn.arorms.android.ht.client.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.pojo.models.Comment
import cn.arorms.android.ht.client.pojo.models.User
import cn.arorms.android.ht.client.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class UserViewModel : ViewModel() {
    private val repository = UserRepository()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadUserProfile(userId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.getUserById(userId)
                _user.value = result
            } catch (exception: Exception) {
                _error.value = "加载用户失败: ${exception.message}"
            }

            _loading.value = false
        }
    }

    fun loadComments(userId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.getCommentsByUserId(userId)
                _comments.value = result
            } catch (exception: Exception) {
                _error.value = "加载评论失败: ${exception.message}"
            }

            _loading.value = false
        }
    }

    fun updateUserProfile(user: User) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.updateUser(user.id!!, user)
                _user.value = result
            } catch (exception: Exception) {
                _error.value = "更新失败: ${exception.message}"
            }

            _loading.value = false
        }
    }

    fun createComment(toUserId: Long, content: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            val currentUserId = AuthManager.getUserId()
            if (currentUserId == 0L) {
                _error.value = "用户未登录"
                _loading.value = false
                return@launch
            }

            try {
                val comment = Comment(
                    fromUserId = currentUserId,
                    toUserId = toUserId,
                    content = content,
                )

                val result = repository.createComment(comment)
                // Reload comments
                loadComments(toUserId)
            } catch (exception: Exception) {
                _error.value = "评论失败: ${exception.message}"
            }

            _loading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
