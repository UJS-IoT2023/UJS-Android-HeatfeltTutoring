package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.entity.Wallet
import cn.arorms.android.ht.server.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/wallets")
class WalletController @Autowired constructor(
    private val walletService: WalletService
) {
    // Get wallet by user ID
    @GetMapping("/user/{userId}")
    fun getWalletByUserId(@PathVariable userId: Long): ResponseEntity<Wallet> {
        val wallet = walletService.getWalletByUserId(userId)
        return if (wallet != null) {
            ResponseEntity(wallet, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Update wallet
    @PutMapping("/{id}")
    fun updateWallet(@PathVariable id: Long, @RequestBody walletDetails: Wallet): ResponseEntity<Wallet> {
        try {
            val updatedWallet = walletService.updateWallet(id, walletDetails)
            return ResponseEntity(updatedWallet, HttpStatus.OK)
        } catch (e: RuntimeException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Delete wallet
    @DeleteMapping("/{id}")
    fun deleteWallet(@PathVariable id: Long): ResponseEntity<Void> {
        try {
            walletService.deleteWallet(id)
            return ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: RuntimeException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
