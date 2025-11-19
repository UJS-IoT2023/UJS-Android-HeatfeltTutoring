package cn.arorms.android.ht.client.models

data class RegisterRequest(
    val phoneNumber: String,
    val password: String,
    val icon: String? = null,
    val address: String? = null
)
