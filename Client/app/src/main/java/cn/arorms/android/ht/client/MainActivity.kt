package cn.arorms.android.ht.client

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.arorms.android.ht.client.databinding.ActivityMainBinding
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.ui.auth.LoginActivity
import cn.arorms.android.ht.client.ui.user.UserFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // 初始化AuthManager
        AuthManager.initialize(this)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // 设置顶级目的地
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.plansFragment,
                R.id.teachersFragment,
                R.id.appointmentsFragment,
                R.id.userFragment,
                R.id.walletFragment,
                R.id.aiChatFragment
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        
        // 初始化导航栏用户信息
        updateNavigationHeader()
        
        // 设置导航菜单点击监听器
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_plans -> {
                    navController.navigate(R.id.plansFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_teachers -> {
                    navController.navigate(R.id.teachersFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_appointments -> {
                    navController.navigate(R.id.appointmentsFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_profile -> {
                    val userId = AuthManager.getUserId()
                    if (userId != 0L) {
                        val bundle = Bundle().apply {
                            putLong("userId", userId)
                        }
                        navController.navigate(R.id.userFragment, bundle)
                        binding.drawerLayout.closeDrawers()
                        true
                    } else {
                        false
                    }
                }
                R.id.nav_wallet -> {
                    navController.navigate(R.id.walletFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_ai_chat -> {
                    navController.navigate(R.id.aiChatFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }
        
        // 如果已登录但没有用户缓存，尝试从服务器获取用户信息
        if (AuthManager.isLoggedIn() && !AuthManager.hasUserCache()) {
            fetchUserInfo()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    
    private fun logout() {
        AuthManager.clear()
        // 直接启动登录Activity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
    
    private fun updateNavigationHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.textViewUserName)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.textViewUserEmail)
        val userAvatarImageView = headerView.findViewById<ImageView>(R.id.imageViewUserAvatar)

        // 优先从缓存的User对象获取信息
        val cachedUser = AuthManager.getUser()
        
        if (cachedUser != null) {
            // 使用缓存的完整用户信息
            userNameTextView.text = cachedUser.username
            userEmailTextView.text = cachedUser.email ?: "未设置联系方式"
            
            // TODO: 加载网络图片（从cachedUser.avatarUrl）
            userAvatarImageView.setImageResource(R.drawable.baseline_person_24)
        } else {
            // 回退到单独的字段
            val userName = AuthManager.getUsername()
            val userPhone = AuthManager.getPhoneNumber()
            val userIcon = AuthManager.getUserIcon()

            userNameTextView.text = if (userName.isNotEmpty()) userName else "用户"
            userEmailTextView.text = if (userPhone.isNotEmpty()) userPhone else "未登录"

            if (userIcon.isNotEmpty()) {
                // TODO: 加载网络图片
                userAvatarImageView.setImageResource(R.drawable.baseline_person_24)
            } else {
                userAvatarImageView.setImageResource(R.drawable.baseline_person_24)
            }
        }
    }
    
    private fun fetchUserInfo() {
        val userId = AuthManager.getUserId()
        if (userId == -1L) return
        
        // 使用协程异步获取用户信息
        lifecycleScope.launch {
            try {
                val apiService = cn.arorms.android.ht.client.network.RetrofitClient.instance
                val user = apiService.getUserById(userId)
                AuthManager.saveUser(user)
                // 更新导航栏显示
                updateNavigationHeader()
            } catch (e: Exception) {
                // 获取失败，静默处理
                e.printStackTrace()
            }
        }
    }
}
