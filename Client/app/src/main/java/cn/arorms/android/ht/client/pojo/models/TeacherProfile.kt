package cn.arorms.android.ht.client.pojo.models

import com.google.gson.annotations.SerializedName

data class TeacherProfile(
    var id: Long? = null,
    var educationalBackground: String? = null,
    var taughtGrades: String? = null,
    @SerializedName("taughtSubject")
    var taughtSubject: String? = null,
    var taughtCourses: String? = null
)
