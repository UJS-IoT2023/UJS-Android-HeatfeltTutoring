package cn.arorms.android.ht.client.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.FragmentLoginBinding
import cn.arorms.android.ht.client.dto.LoginRequest
import cn.arorms.android.ht.client.network.ApiService
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    
    private val apiService: ApiService = RetrofitClient.instance
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etPhoneNumber.setText("13800138000")
        binding.etPassword.setText("password123")

        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            login()
        }
        
        binding.btnRegister.setOnClickListener {
            navigateToRegister()
        }
    }
    
    private fun login() {
        val phoneNumber = binding.etPhoneNumber.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        
        if (phoneNumber.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "请输入手机号和密码", Toast.LENGTH_SHORT).show()
            return
        }
        
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val loginRequest = LoginRequest(phoneNumber, password)
                val response = apiService.login(loginRequest)
                
                // 保存token和用户信息
                AuthManager.saveToken(response.token)
                AuthManager.saveUserId(response.userId)
                AuthManager.savePhoneNumber(response.phoneNumber)
                AuthManager.saveUsername(response.username ?: response.phoneNumber)
                AuthManager.saveUserIcon(response.icon ?: "")
                
                Toast.makeText(requireContext(), "登录成功", Toast.LENGTH_SHORT).show()
                
                // 通过LoginActivity导航到主界面
                (requireActivity() as LoginActivity).navigateToMain()
                
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "登录失败: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
            }
        }
    }
    
    private fun navigateToRegister() {
        (requireActivity() as LoginActivity).navigateToRegister()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
