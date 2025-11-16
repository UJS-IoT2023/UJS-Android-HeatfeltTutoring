package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.models.Reward
import cn.arorms.android.ht.server.service.RewardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rewards")
class RewardController @Autowired constructor(
    private val rewardService: RewardService
) {

    // Get all rewards
    @GetMapping
    fun getAllRewards(): ResponseEntity<List<Reward>> {
        val rewards = rewardService.getAllRewards()
        return ResponseEntity(rewards, HttpStatus.OK)
    }

    // Get reward by ID
    @GetMapping("/{id}")
    fun getRewardById(@PathVariable id: Long): ResponseEntity<Reward> {
        val reward = rewardService.getRewardById(id)
        return if (reward.isPresent) {
            ResponseEntity(reward.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Get rewards by user ID
    @GetMapping("/user/{userId}")
    fun getRewardsByUserId(@PathVariable userId: Long): ResponseEntity<List<Reward>> {
        val rewards = rewardService.getRewardsByUserId(userId)
        return ResponseEntity(rewards, HttpStatus.OK)
    }

    // Create new reward
    @PostMapping
    fun createReward(@RequestBody reward: Reward): ResponseEntity<Reward> {
        val createdReward = rewardService.createReward(reward)
        return ResponseEntity(createdReward, HttpStatus.CREATED)
    }

    // Update reward
    @PutMapping("/{id}")
    fun updateReward(@PathVariable id: Long, @RequestBody rewardDetails: Reward): ResponseEntity<Reward> {
        try {
            val updatedReward = rewardService.updateReward(id, rewardDetails)
            return ResponseEntity(updatedReward, HttpStatus.OK)
        } catch (e: RuntimeException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Delete reward
    @DeleteMapping("/{id}")
    fun deleteReward(@PathVariable id: Long): ResponseEntity<Void> {
        try {
            rewardService.deleteReward(id)
            return ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: RuntimeException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
