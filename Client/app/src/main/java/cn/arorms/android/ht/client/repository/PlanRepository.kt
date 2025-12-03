package cn.arorms.android.ht.client.repository

import cn.arorms.android.ht.client.network.ApiService
import cn.arorms.android.ht.client.network.RetrofitClient
import cn.arorms.android.ht.client.pojo.models.Plan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlanRepository {
    private val apiService: ApiService = RetrofitClient.instance
    
    suspend fun getUserPlans(userId: Long): Result<List<Plan>> {
        return try {
            withContext(Dispatchers.IO) {
                val plans = apiService.getUserPlans(userId)
                Result.success(plans)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAllPlans(): Result<List<Plan>> {
        return try {
            withContext(Dispatchers.IO) {
                val plans = apiService.getAllPlans()
                Result.success(plans)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getPlanById(id: Long): Result<Plan> {
        return try {
            withContext(Dispatchers.IO) {
                val plan = apiService.getPlanById(id)
                Result.success(plan)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getPlansByStatus(isCompleted: Boolean): Result<List<Plan>> {
        return try {
            withContext(Dispatchers.IO) {
                val plans = apiService.getPlansByStatus(isCompleted)
                Result.success(plans)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createPlan(plan: Plan): Result<Plan> {
        return try {
            withContext(Dispatchers.IO) {
                val createdPlan = apiService.createPlan(plan)
                Result.success(createdPlan)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updatePlan(id: Long, plan: Plan): Result<Plan> {
        return try {
            withContext(Dispatchers.IO) {
                val updatedPlan = apiService.updatePlan(id, plan)
                Result.success(updatedPlan)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deletePlan(id: Long): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                apiService.deletePlan(id)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
