package cn.arorms.android.ht.client

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.arorms.android.ht.client.databinding.ActivityMainBinding
import cn.arorms.android.ht.client.network.AuthManager
import cn.arorms.android.ht.client.ui.auth.LoginActivity

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
                R.id.appointmentsFragment
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
                R.id.nav_logout -> {
                    logout()
                    true
                }
                else -> false
            }
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
        val userPhoneTextView = headerView.findViewById<TextView>(R.id.textViewUserPhone)
        val userAvatarImageView = headerView.findViewById<ImageView>(R.id.imageViewUserAvatar)

        val userName = AuthManager.getUsername()
        val userPhone = AuthManager.getPhoneNumber()
        val userIcon = AuthManager.getUserIcon()

        userNameTextView.text = if (userName.isNotEmpty()) userName else "用户"

        userPhoneTextView.text = if (userPhone.isNotEmpty()) userPhone else "未登录"

        if (userIcon.isNotEmpty()) {
            // TODO: 加载网络图片
            userAvatarImageView.setImageResource(R.drawable.baseline_person_24)
        } else {
            userAvatarImageView.setImageResource(R.drawable.baseline_person_24)
        }
    }
}
