package cn.arorms.android.ht.client.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.FragmentRegisterBinding
import cn.arorms.android.ht.client.dto.RegisterRequest
import cn.arorms.android.ht.client.network.ApiService
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.network.RetrofitClient
import kotlinx.coroutines.launch

//private val FragmentRegisterBinding.etUsername: Any

class RegisterFragment : Fragment() {
    
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    
    private val apiService: ApiService = RetrofitClient.instance
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            register()
        }
        
        binding.btnLogin.setOnClickListener {
            navigateToLogin()
        }
    }
    
    private fun register() {
        val email = binding.etEmail.text.toString().trim()
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "请填写所有字段", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "密码不一致", Toast.LENGTH_SHORT).show()
            return
        }
        
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val registerRequest = RegisterRequest(username, email, password)
                val response = apiService.register(registerRequest)
                
                // 保存token和用户信息
                AuthManager.saveToken(response.token)
                AuthManager.saveUserId(response.userId)
//                AuthManager.savePhoneNumber(response.phoneNumber)
                
                Toast.makeText(requireContext(), "注册成功", Toast.LENGTH_SHORT).show()
                
                // 通过LoginActivity导航到主界面
                (requireActivity() as LoginActivity).navigateToMain()
                
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "注册失败: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true
            }
        }
    }
    
    private fun navigateToLogin() {
        requireActivity().supportFragmentManager.popBackStack()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
