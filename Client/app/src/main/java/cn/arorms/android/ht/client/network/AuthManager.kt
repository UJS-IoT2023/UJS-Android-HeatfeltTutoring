package cn.arorms.android.ht.client.network

import android.content.Context
import android.content.SharedPreferences
import cn.arorms.android.ht.client.pojo.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AuthManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_PHONE_NUMBER = "phone_number"
    private const val KEY_USER_NAME = "username"
    private const val KEY_USER_ICON = "user_icon"
    private const val KEY_USER_OBJECT = "user_object"
    
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }
    
    fun getToken(): String {
        return sharedPreferences.getString(KEY_TOKEN, "") ?: ""
    }
    
    fun saveUserId(userId: Long) {
        sharedPreferences.edit().putLong(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Long {
        return sharedPreferences.getLong(KEY_USER_ID, -1L)
    }

    fun savePhoneNumber(phoneNumber: String) {
        sharedPreferences.edit().putString(KEY_PHONE_NUMBER, phoneNumber).apply()
    }

    fun getPhoneNumber(): String {
        return sharedPreferences.getString(KEY_PHONE_NUMBER, "") ?: ""
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString(KEY_USER_NAME, username).apply()
    }

    fun getUsername(): String {
        return sharedPreferences.getString(KEY_USER_NAME, "") ?: ""
    }

    fun saveUserIcon(userIcon: String) {
        sharedPreferences.edit().putString(KEY_USER_ICON, userIcon).apply()
    }

    fun getUserIcon(): String {
        return sharedPreferences.getString(KEY_USER_ICON, "") ?: ""
    }
    
    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString(KEY_USER_OBJECT, userJson).apply()
        
        // 同时保存关键字段到单独的key，便于快速访问
        saveUserId(user.id ?: -1L)
        saveUsername(user.username)
        savePhoneNumber(user.phoneNumber ?: "")
        saveUserIcon(user.avatarUrl ?: "")
    }
    
    fun getUser(): User? {
        val userJson = sharedPreferences.getString(KEY_USER_OBJECT, null)
        return if (userJson != null) {
            try {
                gson.fromJson(userJson, User::class.java)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
    
    fun hasUserCache(): Boolean {
        return sharedPreferences.getString(KEY_USER_OBJECT, null) != null
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
    
    fun isLoggedIn(): Boolean {
        return getToken().isNotEmpty()
    }
    
    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}
