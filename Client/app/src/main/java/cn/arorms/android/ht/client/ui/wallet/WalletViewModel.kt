package cn.arorms.android.ht.client.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.network.RetrofitClient
import cn.arorms.android.ht.client.pojo.models.Wallet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WalletViewModel : ViewModel() {
    private val apiService = RetrofitClient.instance

    private val _wallet = MutableStateFlow<Wallet?>(null)
    val wallet: StateFlow<Wallet?> = _wallet.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadWallet() {
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
                val result = apiService.getWalletByUserId(userId)
                _wallet.value = result
            } catch (exception: Exception) {
                _error.value = "加载钱包失败: ${exception.message}"
            }

            _loading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
