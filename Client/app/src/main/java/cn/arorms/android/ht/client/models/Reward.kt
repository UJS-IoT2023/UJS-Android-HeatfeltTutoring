package cn.arorms.android.ht.client.models

data class Reward(
    val id: Long? = null,
    val userId: Long,
    val points: Int,
    val description: String
)
