package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.pojo.entity.Wallet
import cn.arorms.android.ht.server.repository.UserRepository
import cn.arorms.android.ht.server.repository.WalletRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class WalletService @Autowired constructor(
    private val walletRepository: WalletRepository,
    private val userRepository: UserRepository
) {

//    // Get all wallets
//    fun getAllWallets(): List<Wallet> {
//        return walletRepository.findAll()
//    }
//
//    // Get wallet by ID
//    fun getWalletById(id: Long): Optional<Wallet> {
//        return walletRepository.findById(id)
//    }

    // Get wallet by user ID
    fun getWalletByUserId(userId: Long): Wallet? {
        var wallet = walletRepository.findByUserId(userId)
        if (wallet == null) {
            wallet = createWallet(userId)
            walletRepository.save(wallet)
        }
        return wallet
    }

    // Create new wallet
    fun createWallet(userId: Long): Wallet {
        val wallet = Wallet(
            userId = userId,
            balance = 0.0,
            points = 0.0
        )
        return walletRepository.save(wallet)
    }

    // Update wallet
    fun updateWallet(id: Long, walletDetails: Wallet): Wallet {
        val wallet = walletRepository.findById(id)
            .orElseThrow { RuntimeException("Wallet not found with id: $id") }

        wallet.balance = walletDetails.balance
        wallet.points = walletDetails.points

        return walletRepository.save(wallet)
    }

    // Delete wallet
    fun deleteWallet(id: Long) {
        val wallet = walletRepository.findById(id)
            .orElseThrow { RuntimeException("Wallet not found with id: $id") }
        walletRepository.delete(wallet)
    }

    // Check if wallet exists
    fun existsById(id: Long): Boolean {
        return walletRepository.existsById(id)
    }
}
