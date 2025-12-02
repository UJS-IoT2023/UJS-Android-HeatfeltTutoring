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

    // Get all wallets
    @GetMapping
    fun getAllWallets(): ResponseEntity<List<Wallet>> {
        val wallets = walletService.getAllWallets()
        return ResponseEntity(wallets, HttpStatus.OK)
    }

    // Get wallet by ID
    @GetMapping("/{id}")
    fun getWalletById(@PathVariable id: Long): ResponseEntity<Wallet> {
        val wallet = walletService.getWalletById(id)
        return if (wallet.isPresent) {
            ResponseEntity(wallet.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

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

    // Get wallet by phone number
    @GetMapping("/phone/{phoneNumber}")
    fun getWalletByPhoneNumber(@PathVariable phoneNumber: String): ResponseEntity<Wallet> {
        val wallet = walletService.getWalletByPhoneNumber(phoneNumber)
        return if (wallet != null) {
            ResponseEntity(wallet, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Create new wallet
    @PostMapping
    fun createWallet(@RequestBody wallet: Wallet): ResponseEntity<Wallet> {
        val createdWallet = walletService.createWallet(wallet)
        return ResponseEntity(createdWallet, HttpStatus.CREATED)
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
