package cn.arorms.android.ht.client.repository

import cn.arorms.android.ht.client.network.RetrofitClient
import cn.arorms.android.ht.client.pojo.models.Comment
import cn.arorms.android.ht.client.pojo.models.User

class UserRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getUserById(id: Long): User {
        return apiService.getUserById(id)
    }

    suspend fun updateUser(id: Long, user: User): User {
        return apiService.updateUser(id, user)
    }

    suspend fun getCommentsByUserId(userId: Long): List<Comment> {
        return apiService.getCommentsByUserId(userId)
    }

    suspend fun createComment(comment: Comment): Comment {
        return apiService.createComment(comment)
    }
}
