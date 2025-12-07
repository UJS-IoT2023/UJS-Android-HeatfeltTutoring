package cn.arorms.android.ht.client.pojo.models

data class Wallet(
    val id: Long? = null,
    val user: User,
    val balance: Double,
    val points: Double
)
