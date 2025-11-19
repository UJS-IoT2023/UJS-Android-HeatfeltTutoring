package cn.arorms.android.ht.client.models

data class Teacher(
    val id: Long? = null,
    val phoneNumber: String,
    val sex: String,
    val name: String,
    val icon: String,
    val address: String,
    val educationalBackground: String,
    val taughtGrades: String
)
