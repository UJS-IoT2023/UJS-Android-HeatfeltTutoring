package cn.arorms.android.ht.server.pojo.dto

data class WalletDto(
    var id: Long? = null,
    val userId: Long,
    var username: String? = null,
    var balance: Double,
    var points: Double
)
