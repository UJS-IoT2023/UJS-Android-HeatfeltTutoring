package cn.arorms.android.ht.client.models

data class Order(
    val id: Long? = null,
    val userId: Long,
    val teacherId: Long,
    val subject: String,
    val price: Double,
    val status: String
)
