package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.entity.Wallet
import com.fasterxml.jackson.annotation.JsonCreator
import org.aspectj.weaver.ast.Var

data class WalletDto @JsonCreator constructor(
    var id: Long? = null,
    val userId: Long? = null,
    var balance: Double,
    var points: Double
) {
    constructor(wallet: Wallet): this(
        id = wallet.id,
        userId = wallet.userId,
        balance = wallet.balance,
        points = wallet.points
    )
}
