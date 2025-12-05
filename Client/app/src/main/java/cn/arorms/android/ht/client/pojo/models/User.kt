package cn.arorms.android.ht.client.pojo.models

import java.time.LocalDateTime

data class User(
    // ===== Auth information =====
    var id: Long? = null,
    var username: String,
    var email: String,
    var password: String,

    // ===== Extended info =====
    var wechatOpenid: String? = null,
    var qqOpenid: String? = null,
    var wallet: Wallet? = null,

    // ===== Profile information =====
    var phoneNumber: String? = null,
    var avatarUrl: String? = null,
    var realName: String? = null,
    var wechatId: String? = null,
    var qqId: String? = null,
    var address: String? = null,
    var createdAt: LocalDateTime = LocalDateTime.now()
)
