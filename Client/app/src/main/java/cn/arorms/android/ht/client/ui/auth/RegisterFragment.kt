package cn.arorms.android.ht.client.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cn.arorms.android.ht.client.R
import cn.arorms.android.ht.client.databinding.FragmentRegisterBinding
import cn.arorms.android.ht.client.network.ApiService
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.network.RetrofitClient
import cn.arorms.android.ht.client.pojo.dto.LoginRequest
import cn.arorms.android.ht.client.pojo.dto.LoginType
import cn.arorms.android.ht.client.pojo.dto.RegisterRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import cn.arorms.android.ht.client.pojo.dto.RegisterType
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

//private val FragmentRegisterBinding.etUsername: Any

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val apiService: ApiService = RetrofitClient.instance
    private lateinit var credentialManager: CredentialManager
    
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

        setupGoogleSignIn()
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            register()
        }

        binding.btnGoogleRegister.setOnClickListener {
            googleSignIn()
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
                val registerRequest = RegisterRequest(RegisterType.EMAIL, username, email, password)
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

    private fun setupGoogleSignIn() {
        credentialManager = CredentialManager.create(requireContext())
    }

    private fun googleSignIn() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(requireActivity(), request)
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleId = googleIdTokenCredential.id
                val displayName = googleIdTokenCredential.displayName ?: "Google User"

                // Use placeholder email for Google users
                val email = googleId

                Log.d("GoogleSignIn", "Successfully signed in user: $displayName, Google ID: $googleId, Email: $email")

                // For registration, create account with Google credentials
                registerWithGoogle(googleId, displayName, email)

            } catch (e: GetCredentialException) {
                Log.e("GoogleSignIn", "Google sign in failed", e)
                Toast.makeText(requireContext(), "Google注册失败: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }



    private fun registerWithGoogle(googleId: String, displayName: String, email: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnGoogleRegister.isEnabled = false
        binding.btnRegister.isEnabled = false

        lifecycleScope.launch {
            try {
                // For Google registration, use register endpoint with Google user info
                // Use Google ID as password since Google users don't have passwords
                val registerRequest = RegisterRequest(RegisterType.GOOGLE, displayName, email, googleId)
                val response = apiService.register(registerRequest)

                // 保存token和用户ID
                AuthManager.saveToken(response.token)
                AuthManager.saveUserId(response.userId)

                Toast.makeText(requireContext(), "Google注册成功", Toast.LENGTH_SHORT).show()

                // 导航到主界面
                (requireActivity() as LoginActivity).navigateToMain()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Google注册失败: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnGoogleRegister.isEnabled = true
                binding.btnRegister.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
