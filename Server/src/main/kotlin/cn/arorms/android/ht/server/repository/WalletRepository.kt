package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.models.Wallet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository : JpaRepository<Wallet, Long> {
    fun findByUserId(userId: Long): Wallet?
    fun findByPhoneNumber(phoneNumber: String): Wallet?
}
