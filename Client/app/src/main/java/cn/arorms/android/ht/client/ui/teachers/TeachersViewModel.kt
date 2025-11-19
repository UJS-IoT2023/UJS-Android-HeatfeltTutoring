package cn.arorms.android.ht.client.ui.teachers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.arorms.android.ht.client.models.Teacher
import cn.arorms.android.ht.client.repository.TeacherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeachersViewModel : ViewModel() {
    private val repository = TeacherRepository()
    
    private val _teachers = MutableStateFlow<List<Teacher>>(emptyList())
    val teachers: StateFlow<List<Teacher>> = _teachers.asStateFlow()
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadTeachers() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            
            val result = repository.getAllTeachers()
            result.onSuccess { teachers ->
                _teachers.value = teachers
            }.onFailure { exception ->
                _error.value = "加载失败: ${exception.message}"
            }
            
            _loading.value = false
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
