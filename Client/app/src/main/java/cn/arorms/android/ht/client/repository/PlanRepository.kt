package cn.arorms.android.ht.client.repository

import cn.arorms.android.ht.client.network.ApiService
import cn.arorms.android.ht.client.network.RetrofitClient
import cn.arorms.android.ht.client.pojo.models.Plan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

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
    
    suspend fun togglePlanCompletion(id: Long): Result<Plan> {
        return try {
            withContext(Dispatchers.IO) {
                val toggledPlan = apiService.togglePlanCompletion(id)
                Result.success(toggledPlan)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deletePlan(id: Long): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.deletePlan(id)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("删除失败，状态码: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
