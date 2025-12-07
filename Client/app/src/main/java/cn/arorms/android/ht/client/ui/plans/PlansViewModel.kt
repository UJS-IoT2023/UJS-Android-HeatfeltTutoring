package cn.arorms.android.ht.client.ui.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.pojo.models.Plan
import cn.arorms.android.ht.client.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlansViewModel : ViewModel() {
    private val repository = PlanRepository()
    
    private val _plans = MutableStateFlow<List<Plan>>(emptyList())
    val plans: StateFlow<List<Plan>> = _plans.asStateFlow()
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadUserPlans() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val userId = AuthManager.getUserId()
            if (userId == 0L) {
                _error.value = "用户未登录"
                _loading.value = false
                return@launch
            }
            
            val result = repository.getUserPlans(userId)
            result.onSuccess { plans ->
                _plans.value = plans
            }.onFailure { exception ->
                _error.value = "加载失败: ${exception.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun createPlan(content: String, deadline: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val userId = AuthManager.getUserId()
            if (userId == 0L) {
                _error.value = "用户未登录"
                _loading.value = false
                return@launch
            }
            
            try {
                val plan = Plan(
                    userId = userId,
                    content = content,
                    deadline = deadline,
                    isCompleted = false
                )
                
                val result = repository.createPlan(plan)
                result.onSuccess { createdPlan ->
                    // 重新加载计划列表
                    loadUserPlans()
                }.onFailure { exception ->
                    _error.value = "创建失败: ${exception.message}"
                }
            } catch (e: Exception) {
                _error.value = "日期格式错误: ${e.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun updatePlanCompletion(planId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val result = repository.togglePlanCompletion(planId)
            
            result.onSuccess { updatedPlan ->
                // 使用服务器返回的更新后的计划更新本地列表
                _plans.value = _plans.value.map { plan ->
                    if (plan.id == planId) updatedPlan else plan
                }
            }.onFailure { exception ->
                _error.value = "更新失败: ${exception.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun deletePlan(planId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val result = repository.deletePlan(planId)
            result.onSuccess { 
                // 从本地列表中移除
                _plans.value = _plans.value.filter { it.id != planId }
            }.onFailure { exception ->
                _error.value = "删除失败: ${exception.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
