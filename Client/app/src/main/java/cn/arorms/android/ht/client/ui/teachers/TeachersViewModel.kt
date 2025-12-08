package cn.arorms.android.ht.client.ui.teachers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.arorms.android.ht.client.pojo.dto.TeacherQueryRequest
import cn.arorms.android.ht.client.pojo.enums.Subject
import cn.arorms.android.ht.client.pojo.models.User
import cn.arorms.android.ht.client.repository.TeacherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeachersViewModel : ViewModel() {
    private val repository = TeacherRepository()

    private val _teachers = MutableStateFlow<List<User>>(emptyList())
    val teachers: StateFlow<List<User>> = _teachers.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword.asStateFlow()

    private val _selectedSubject = MutableStateFlow<Subject?>(null)
    val selectedSubject: StateFlow<Subject?> = _selectedSubject.asStateFlow()

    fun loadTeachers() {
        searchTeachers("", null)
    }

    fun searchTeachers(keyword: String = _keyword.value, subject: Subject? = _selectedSubject.value) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            _keyword.value = keyword
            _selectedSubject.value = subject

            val query = TeacherQueryRequest(
                keyword = keyword.takeIf { it.isNotBlank() },
                subject = subject
            )

            val result = repository.searchTeachers(query)
            result.onSuccess { teachers ->
                _teachers.value = teachers
            }.onFailure { exception ->
                _error.value = "搜索失败: ${exception.message}"
            }

            _loading.value = false
        }
    }

    fun setKeyword(keyword: String) {
        _keyword.value = keyword
    }

    fun setSelectedSubject(subject: Subject?) {
        _selectedSubject.value = subject
    }

    fun clearError() {
        _error.value = null
    }
}
