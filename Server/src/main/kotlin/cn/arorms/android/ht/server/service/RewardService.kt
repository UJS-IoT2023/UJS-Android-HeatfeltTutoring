package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.pojo.entity.Reward
import cn.arorms.android.ht.server.repository.RewardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class RewardService @Autowired constructor(
    private val rewardRepository: RewardRepository
) {

    // Get all rewards
    fun getAllRewards(): List<Reward> {
        return rewardRepository.findAll()
    }

    // Get reward by ID
    fun getRewardById(id: Long): Optional<Reward> {
        return rewardRepository.findById(id)
    }

    // Get rewards by user ID
    fun getRewardsByUserId(userId: Long): List<Reward> {
        return rewardRepository.findByUserId(userId)
    }

    // Create new reward
    fun createReward(reward: Reward): Reward {
        return rewardRepository.save(reward)
    }

    // Update reward
    fun updateReward(id: Long, rewardDetails: Reward): Reward {
        val reward = rewardRepository.findById(id)
            .orElseThrow { RuntimeException("Reward not found with id: $id") }

        reward.user = rewardDetails.user
        reward.account = rewardDetails.account

        return rewardRepository.save(reward)
    }

    // Delete reward
    fun deleteReward(id: Long) {
        val reward = rewardRepository.findById(id)
            .orElseThrow { RuntimeException("Reward not found with id: $id") }
        rewardRepository.delete(reward)
    }

    // Check if reward exists
    fun existsById(id: Long): Boolean {
        return rewardRepository.existsById(id)
    }
}
