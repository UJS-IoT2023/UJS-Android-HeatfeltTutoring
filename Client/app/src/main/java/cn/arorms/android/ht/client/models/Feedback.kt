package cn.arorms.android.ht.client.models

data class Feedback(
    val id: Long? = null,
    val content: String,
    val rating: Int,
    val userId: Long,
    val teacherId: Long
)
