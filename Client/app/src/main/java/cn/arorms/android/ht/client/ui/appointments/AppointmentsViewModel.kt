package cn.arorms.android.ht.client.ui.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.arorms.android.ht.client.pojo.models.Appointment
import cn.arorms.android.ht.client.repository.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentsViewModel : ViewModel() {
    private val repository = AppointmentRepository()
    
    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadAppointments() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val result = repository.getAllAppointments()
            result.onSuccess { appointments ->
                _appointments.value = appointments
            }.onFailure { exception ->
                _error.value = "加载预约失败: ${exception.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun loadAppointmentsByUserId(userId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val result = repository.getAppointmentsByUserId(userId)
            result.onSuccess { appointments ->
                _appointments.value = appointments
            }.onFailure { exception ->
                _error.value = "加载用户预约失败: ${exception.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun loadAppointmentsByTeacherId(teacherId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val result = repository.getAppointmentsByTeacherId(teacherId)
            result.onSuccess { appointments ->
                _appointments.value = appointments
            }.onFailure { exception ->
                _error.value = "加载教师预约失败: ${exception.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun createAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val result = repository.createAppointment(appointment)
            result.onSuccess { createdAppointment ->
                // 重新加载预约列表
                loadAppointments()
            }.onFailure { exception ->
                _error.value = "创建预约失败: ${exception.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun updateAppointment(id: Long, appointment: Appointment) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val result = repository.updateAppointment(id, appointment)
            result.onSuccess { updatedAppointment ->
                // 重新加载预约列表
                loadAppointments()
            }.onFailure { exception ->
                _error.value = "更新预约失败: ${exception.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun deleteAppointment(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val result = repository.deleteAppointment(id)
            result.onSuccess {
                // 重新加载预约列表
                loadAppointments()
            }.onFailure { exception ->
                _error.value = "删除预约失败: ${exception.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
