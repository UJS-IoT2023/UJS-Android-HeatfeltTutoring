package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.entity.Plan
import cn.arorms.android.ht.server.service.PlanService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/plans")
class PlanController @Autowired constructor(
    private val planService: PlanService
) {

    // Get all plans
    @GetMapping
    fun getAllPlans(): ResponseEntity<List<Plan>> {
        val plans = planService.getAllPlans()
        return ResponseEntity(plans, HttpStatus.OK)
    }

    // Get plan by ID
    @GetMapping("/{id}")
    fun getPlanById(@PathVariable id: Long): ResponseEntity<Plan> {
        val plan = planService.getPlanById(id)
        return if (plan.isPresent) {
            ResponseEntity(plan.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Get plans by user ID
    @GetMapping("/user/{userId}")
    fun getPlansByUserId(@PathVariable userId: Long): ResponseEntity<List<Plan>> {
        val plans = planService.getPlansByUserId(userId)
        return ResponseEntity(plans, HttpStatus.OK)
    }

    // Get plans by completion status
    @GetMapping("/status/{isCompleted}")
    fun getPlansByCompletionStatus(@PathVariable isCompleted: Boolean): ResponseEntity<List<Plan>> {
        val plans = planService.getPlansByCompletionStatus(isCompleted)
        return ResponseEntity(plans, HttpStatus.OK)
    }

    // Create new plan
    @PostMapping
    fun createPlan(@RequestBody plan: Plan): ResponseEntity<Plan> {
        val createdPlan = planService.createPlan(plan)
        return ResponseEntity(createdPlan, HttpStatus.CREATED)
    }

    // Update plan
    @PutMapping("/{id}")
    fun updatePlan(@PathVariable id: Long, @RequestBody planDetails: Plan): ResponseEntity<Plan> {
        try {
            val updatedPlan = planService.updatePlan(id, planDetails)
            return ResponseEntity(updatedPlan, HttpStatus.OK)
        } catch (e: RuntimeException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    // Delete plan
    @DeleteMapping("/{id}")
    fun deletePlan(@PathVariable id: Long): ResponseEntity<Void> {
        try {
            planService.deletePlan(id)
            return ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: RuntimeException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
