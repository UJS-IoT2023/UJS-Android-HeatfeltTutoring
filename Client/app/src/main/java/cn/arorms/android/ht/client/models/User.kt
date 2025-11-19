package cn.arorms.android.ht.client.models

import java.time.LocalDateTime

data class User(
    val id: Long? = null,
    val userName: String? = null,
    val phoneNumber: String,
    val password: String,
    val icon: String? = null,
    val address: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
