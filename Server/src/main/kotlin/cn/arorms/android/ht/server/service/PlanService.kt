package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.pojo.entity.Plan
import cn.arorms.android.ht.server.repository.PlanRepository
import com.aliyuncs.auth.ISignatureComposer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class PlanService @Autowired constructor(
    private val planRepository: PlanRepository
) {

    // Get all plans
    fun getAllPlans(): List<Plan> {
        return planRepository.findAll()
    }

    // Get plan by ID
    fun getPlanById(id: Long): Optional<Plan> {
        return planRepository.findById(id)
    }

    // Get plans by user ID
    fun getPlansByUserId(userId: Long): List<Plan> {
        return planRepository.findByUserId(userId)
    }

    // Get plans by completion status
    fun getPlansByCompletionStatus(isCompleted: Boolean): List<Plan> {
        return planRepository.findByIsCompleted(isCompleted)
    }

    // Create new plan
    fun createPlan(plan: Plan): Plan {
        return planRepository.save(plan)
    }

    // Toggle plan completion
    fun togglePlanCompletion(id: Long): Plan {
        val plan = planRepository.findById(id)
            .orElseThrow { RuntimeException("Plan not found with id: $id") }
        plan.isCompleted = !plan.isCompleted
        return planRepository.save(plan)
    }
    
    // Update plan
    fun updatePlan(id: Long, planDetails: Plan): Plan {
        val plan = planRepository.findById(id)
            .orElseThrow { RuntimeException("Plan not found with id: $id") }

        plan.user = planDetails.user
        plan.content = planDetails.content
        plan.deadline = planDetails.deadline
        plan.isCompleted = planDetails.isCompleted

        return planRepository.save(plan)
    }

    // Delete plan
    fun deletePlan(id: Long) {
        val plan = planRepository.findById(id)
            .orElseThrow { RuntimeException("Plan not found with id: $id") }
        planRepository.delete(plan)
    }

    // Check if plan exists
    fun existsById(id: Long): Boolean {
        return planRepository.existsById(id)
    }
}
